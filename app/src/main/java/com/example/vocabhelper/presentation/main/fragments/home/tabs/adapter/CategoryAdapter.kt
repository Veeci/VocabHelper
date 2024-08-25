package com.example.vocabhelper.presentation.main.fragments.home.tabs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabhelper.R
import com.example.vocabhelper.data.models.Category
import com.example.vocabhelper.databinding.ItemCategoryBinding

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() 
{
    private val categories: List<Category> = listOf(
        Category("Friends", ""),
        Category("Environment", ""),
        Category("Technology", ""),
        Category("Health", ""),
        Category("Travel", ""),
        Category("Food", ""),
        Category("Arts", ""),
        Category("Business", ""),
        Category("Sports", ""),
        Category("Other", "")
    )

    class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)
    {
        val categoryName = binding.categoryName
        val categoryImage = binding.categoryImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryItem = categories[position]
        holder.binding.apply {
            categoryName.text = categoryItem.name
            when(categoryItem.name)
            {
                "Friends" -> categoryImage.setImageResource(R.drawable.img_friends_category)
                "Environment" -> categoryImage.setImageResource(R.drawable.img_natural_category)
                "Technology" -> categoryImage.setImageResource(R.drawable.img_technology_category)
                "Health" -> categoryImage.setImageResource(R.drawable.img_health_category)
                "Travel" -> categoryImage.setImageResource(R.drawable.img_travel_category)
                "Food" -> categoryImage.setImageResource(R.drawable.img_food_category)
                "Arts" -> categoryImage.setImageResource(R.drawable.img_art_category)
                "Business" -> categoryImage.setImageResource(R.drawable.img_business_category)
                "Sports" -> categoryImage.setImageResource(R.drawable.img_sport_category)
                "Other" -> categoryImage.setImageResource(R.drawable.img_other_category)
            }
        }
    }

}