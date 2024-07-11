package com.example.foodapp.ui.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.Data.Meals
import com.example.foodapp.Utils.CircularTransformation
import com.example.foodapp.databinding.FoodItemBinding
import com.example.foodapp.databinding.ItemCategoryBinding
import com.squareup.picasso.Picasso

class MealsAdapter(val onItemClick: (Meals) -> Unit):RecyclerView.Adapter<MealsAdapter.ViewHolder>(){

    var meals: List<Meals> = emptyList()
        set(value) {
            field= value
            notifyDataSetChanged()
        }

    inner class ViewHolder (val binding: FoodItemBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsAdapter.ViewHolder {
        val binding = FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealsAdapter.ViewHolder, position: Int) {
        val meal = meals[position]

        with(holder.binding){
            MealName.text = meal.strMeal.toString()

            Picasso.get().load(meal.strMealThumb).transform(CircularTransformation()).into(FoodImage)

            root.setOnClickListener {
                onItemClick(meal)
            }
        }
    }

    override fun getItemCount(): Int = meals.size
}