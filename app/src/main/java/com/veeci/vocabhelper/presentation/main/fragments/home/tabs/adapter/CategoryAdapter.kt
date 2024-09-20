package com.veeci.vocabhelper.presentation.main.fragments.home.tabs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.veeci.vocabhelper.R
import com.veeci.vocabhelper.data.models.Category
import com.veeci.vocabhelper.databinding.ItemCategoryBinding

class CategoryAdapter(private val onItemClick: (Category) -> Unit): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var categories: List<Category> = listOf(
        Category(R.string.category_friends, R.drawable.img_friends_category),
        Category(R.string.category_environment, R.drawable.img_natural_category),
        Category(R.string.category_technology, R.drawable.img_technology_category),
        Category(R.string.category_health, R.drawable.img_health_category),
        Category(R.string.category_travel, R.drawable.img_travel_category),
        Category(R.string.category_food, R.drawable.img_food_category),
        Category(R.string.category_arts, R.drawable.img_art_category),
        Category(R.string.category_business, R.drawable.img_business_category),
        Category(R.string.category_sports, R.drawable.img_sport_category),
        Category(R.string.category_other, R.drawable.img_other_category)
    )

    class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val categoryName = binding.categoryName
        val categoryImage = binding.categoryImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val context = holder.itemView.context
        val categoryItem = categories[position]
        holder.binding.apply {
            categoryName.text = context.getString(categoryItem.nameResId)
            categoryImage.setImageResource(categoryItem.imageResId)
            root.setOnClickListener {
                onItemClick(categoryItem)
            }
        }
    }
}