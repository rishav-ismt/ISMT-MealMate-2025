package np.com.ismt.sample.mealmate.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import np.com.ismt.sample.mealmate.databinding.FragmentRecipesBinding
import np.com.ismt.sample.mealmate.helpers.VerticalSpacingDecorator
import np.com.ismt.sample.mealmate.recipe.Recipe

class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var db: FirebaseFirestore? = null
    private lateinit var adapter: RecipesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val recipesViewModel =
            ViewModelProvider(this)[RecipesViewModel::class.java]

        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db?.apply {
            setUpRecipesAdapter()
            _binding?.rvRecipes?.layoutManager = LinearLayoutManager(requireActivity())
            _binding?.rvRecipes?.apply {
                this.layoutManager = LinearLayoutManager(requireActivity())
                this.addItemDecoration(
                    VerticalSpacingDecorator(
                        top = 24,
                        start = 0,
                        end = 0,
                        bottom = 0
                    )
                )
            }
            _binding?.rvRecipes?.adapter = adapter
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecipesAdapter() {
        val query = db!!.collection("recipe")

        val options = FirestoreRecyclerOptions.Builder<Recipe>()
            .setQuery(query, Recipe::class.java)
            .build()

        adapter = RecipesAdapter(options)
    }
}