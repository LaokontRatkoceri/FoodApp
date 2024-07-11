package com.example.foodapp.ui.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.Data.Meals
import com.example.foodapp.Data.mealsR
import com.example.foodapp.Utils.CircularTransformation
import com.example.foodapp.databinding.RendomSelectionBinding
import com.squareup.picasso.Picasso

class RandomAdapter(val onItemClick: (mealsR) -> Unit):RecyclerView.Adapter<RandomAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RendomSelectionBinding) : RecyclerView.ViewHolder(binding.root){

    }

    var meals: List<mealsR> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomAdapter.ViewHolder {
        val binding = RendomSelectionBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RandomAdapter.ViewHolder, position: Int) {
        val meal = meals[position]

        with(holder.binding) {
            MealsName.text = meal.strMeal
            Picasso.get().load(meal.strMealThumb).transform(CircularTransformation())
                .into(ImageFood)

            root.setOnClickListener {
                onItemClick(meal)
            }
        }


    }

    override fun getItemCount(): Int = meals.size
}