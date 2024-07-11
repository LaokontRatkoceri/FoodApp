package com.example.foodapp.ui.Home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.Data.Categories
import com.example.foodapp.databinding.ItemCategoryBinding
import kotlin.reflect.KFunction1

class CategoriesAdapter(val onItemClick: (String) -> Unit ): RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private var selectedPosition: Int = -1

    var categories: List<Categories> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        with(holder.binding) {
            Category.text = category.strCategory

            // Check if the current position is the selected position
            if (position == selectedPosition) {
                Category.setTextColor(Color.WHITE)
                ImageSelect.visibility = View.VISIBLE
            } else {
                Category.setTextColor(Color.parseColor("#129575"))
                ImageSelect.visibility = View.GONE
            }

            root.setOnClickListener {
                // Toggle selection on item click
                selectedPosition = position
                notifyDataSetChanged() // Notify adapter to update UI
                onItemClick(category.strCategory.toString())
            }
        }
    }
}