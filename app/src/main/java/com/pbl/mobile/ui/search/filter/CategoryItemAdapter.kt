package com.pbl.mobile.ui.search.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pbl.mobile.R
import com.pbl.mobile.databinding.ItemCategoryBinding
import com.pbl.mobile.model.dto.CategoryTagItem

class CategoryItemAdapter(
    private val categories: ArrayList<CategoryTagItem>,
    private val onItemSelected: (item: CategoryTagItem) -> Unit
) : RecyclerView.Adapter<CategoryItemAdapter.CategoryItemViewHolder>() {


    inner class CategoryItemViewHolder(val binding: ItemCategoryBinding) :
        ViewHolder(binding.root) {

        fun bind(item: CategoryTagItem) {
            binding.apply {
                tvCategoryItem.text = item.name
                if (item.isSelected) {
                    val resources = root.context.resources
                    tvCategoryItem.setTextColor(
                        resources.getColor(
                            R.color.white_100,
                            root.context.theme
                        )
                    )
                    root.setBackgroundResource(R.drawable.bg_category_item_selected)
                } else {
                    val resources = root.context.resources
                    tvCategoryItem.setTextColor(
                        resources.getColor(
                            R.color.black_100,
                            root.context.theme
                        )
                    )
                    root.setBackgroundResource(R.drawable.bg_category_item)
                }
                root.setOnClickListener {
                    item.isSelected = !item.isSelected
                    notifyItemChanged(absoluteAdapterPosition)
                    onItemSelected.invoke(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        return CategoryItemViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount() = categories.size
}
