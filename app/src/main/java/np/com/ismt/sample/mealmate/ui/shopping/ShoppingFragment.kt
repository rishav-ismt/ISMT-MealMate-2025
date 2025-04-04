package np.com.ismt.sample.mealmate.ui.shopping

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import np.com.ismt.sample.mealmate.databinding.FragmentShoppingBinding
import np.com.ismt.sample.mealmate.helpers.HelperUtil
import np.com.ismt.sample.mealmate.helpers.VerticalSpacingDecorator
import np.com.ismt.sample.mealmate.models.Grocery
import np.com.ismt.sample.mealmate.ui.maps.MapsActivity

class ShoppingFragment : Fragment() {

    private var _binding: FragmentShoppingBinding? = null
    private val binding get() = _binding!!

    private var db: FirebaseFirestore? = null
    private lateinit var groceryShoppingAdapter: GroceryShoppingAdapter
    private var groceries = mutableListOf<Grocery>()
    private var storeLatitude = ""
    private var storeLongitude = ""

    private var startMapActivityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == MapsActivity.MAPS_ACTIVITY_SUCCESS_RESULT_CODE) {
            storeLatitude = it.data?.getStringExtra("latitude").toString()
            storeLongitude = it.data?.getStringExtra("longitude").toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        setUpRecyclerView()
        fetchRecipesAddedToMeal()
        setUpMapPage()
        setUpSendSms()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpAdapter() {
        groceryShoppingAdapter = GroceryShoppingAdapter(groceries)
    }

    private fun setUpRecyclerView() {
        binding.rvGrocery.apply {
            this.layoutManager = LinearLayoutManager(requireActivity())
            this.addItemDecoration(
                VerticalSpacingDecorator(
                    top = 0,
                    start = 0,
                    end = 0,
                    bottom = 24
                )
            )
            this.adapter = groceryShoppingAdapter
        }
    }

    private fun fetchRecipesAddedToMeal() {
        db?.apply {
            this.collection("recipes")
                .whereEqualTo("isSelectedForMeal", true)
                .get()
                .addOnSuccessListener { results ->
                    validateRecipeFetchData(results)
                }
                .addOnFailureListener { error ->
                    Log.d("MealsFragment", "Unable to fetchRecipes: ".plus(error.message))
                }
        }
    }

    private fun validateRecipeFetchData(results: QuerySnapshot) {
        if (results.isEmpty) {
            binding.tvShoppingNotice.visibility = View.VISIBLE
            binding.tvTitle.visibility = View.GONE
            binding.rvGrocery.visibility = View.GONE
            return
        } else {
            binding.tvShoppingNotice.visibility = View.GONE
            binding.tvTitle.visibility = View.VISIBLE
            binding.rvGrocery.visibility = View.VISIBLE
        }
        groceries.clear()
        for (document in results) {
            try {
                val recipeData = document.data
                val ingredients = recipeData["ingredients"] as? List<Map<String, Any>>

                ingredients?.let {
                    for (ingredient in it) {
                        val name = ingredient["name"] as? String ?: ""
                        groceries.add(Grocery(name = name))
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }

        groceryShoppingAdapter.notifyItemRangeInserted(0, groceries.count())
    }

    private fun setUpMapPage() {
       binding.fabMaps.setOnClickListener {
           val intent = Intent(requireActivity(), MapsActivity::class.java)
           startMapActivityForResult.launch(intent)
       }
    }

    private fun setUpSendSms() {
        binding.fabShare.setOnClickListener {
            if (storeLatitude.isEmpty() || storeLongitude.isEmpty()) {
                HelperUtil.showToastMessage(requireActivity(), "Please locate your favourite store in the map")
            } else {
                var storeLocation = "https://www.google.com/maps?q=${storeLatitude},${storeLongitude}"


                var dataToSend = StringBuilder()
                dataToSend.append("Groceries")
                groceries.filter { !it.isPurchased }.forEach {
                    dataToSend.append("\n")
                    dataToSend.append("- ".plus(it.name))
                }

                dataToSend.append("\n\n")
                dataToSend.append("Store location :\n")
                dataToSend.append(storeLocation)

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, dataToSend.toString())
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }
}