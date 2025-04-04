package np.com.ismt.sample.mealmate.ui.shopping

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import np.com.ismt.sample.mealmate.databinding.LayoutGroceryShoppingItemBinding
import np.com.ismt.sample.mealmate.models.Grocery


class GroceryShoppingAdapter(
    private val groceries: List<Grocery>
): RecyclerView.Adapter<GroceryShoppingAdapter.GroceryShoppingViewHolder>() {

    inner class GroceryShoppingViewHolder(
        private val binding: LayoutGroceryShoppingItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindGrocery(grocery: Grocery) {
            binding.apply {
                tvGroceryName.text = grocery.name
                cbGrocery.setOnCheckedChangeListener { _, checked ->
                    if (checked) {
                        cbGrocery.isChecked = true
                        grocery.isPurchased = true
                        tvGroceryName.paintFlags = tvGroceryName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    } else {
                        cbGrocery.isChecked = false
                        grocery.isPurchased = false
                        tvGroceryName.paintFlags = Paint.ANTI_ALIAS_FLAG
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryShoppingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutGroceryShoppingItemBinding.inflate(layoutInflater)
        return GroceryShoppingViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return groceries.size
    }

    override fun onBindViewHolder(holder: GroceryShoppingViewHolder, position: Int) {
        holder.bindGrocery(groceries[position])
    }
}