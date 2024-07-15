package com.example.foodapp.ui.Saved

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.Data.Meals
import com.example.foodapp.Data.mealsR
import com.example.foodapp.Utils.CircularTransformation
import com.example.foodapp.databinding.SavedItemBinding
import com.example.foodapp.databinding.SearchFragmentBinding
import com.example.foodapp.ui.Home.MealsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.squareup.picasso.Picasso

class SavedAdapter: RecyclerView.Adapter<SavedAdapter.ViewHolder>() {

    private lateinit var auth: FirebaseAuth

    private lateinit var database: DatabaseReference

    var meals: List<mealsR> = emptyList()
        set(value) {
            field= value
            notifyDataSetChanged()
        }

    inner class ViewHolder(val binding: SavedItemBinding) :RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SavedItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int  = meals.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = meals[position]

        with(holder.binding){

            MealText.text = meal.strMeal
            Picasso.get().load(meal.strMealThumb).into(MealImage)

        }
    }

}