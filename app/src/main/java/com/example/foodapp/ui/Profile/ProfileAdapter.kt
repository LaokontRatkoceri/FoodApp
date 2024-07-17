package com.example.foodapp.ui.Profile

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.Data.mealsR
import com.example.foodapp.Utils.CircularTransformation
import com.example.foodapp.databinding.FoodItemBinding
import com.example.foodapp.databinding.ProfileItemBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.database
import com.squareup.picasso.Picasso

class ProfileAdapter: RecyclerView.Adapter<ProfileAdapter.ViewHolder>(){

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    var meals: List<mealsR> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder (val binding: ProfileItemBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProfileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = meals.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = meals[position]

        auth = Firebase.auth
        database = Firebase.database.reference

        with(holder.binding) {
            MealText.text = meal.strMeal
            Picasso.get().load(meal.strMealThumb).transform(CircularTransformation())
                .into(MealImage)

            val user = auth.currentUser

            user?.let { currentUser ->
                val userRef = database.child("users").child(currentUser.uid).child("favorites")

                // Fetch the current list of favorite meals
                userRef.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val snapshot = task.result
                        val currentFavorites = snapshot.getValue(object : GenericTypeIndicator<List<String>>() {}) ?: emptyList()

                        // Update UI based on current favorites
                        if (currentFavorites.contains(meal.idMeal.toString())) {
                            SaveImageView.visibility = View.GONE
                            SavedImageView.visibility = View.VISIBLE
                            savedImg.visibility = View.VISIBLE
                            saveImg.visibility = View.GONE
                        } else {
                            SaveImageView.visibility = View.VISIBLE
                            SavedImageView.visibility = View.GONE
                            savedImg.visibility = View.GONE
                            saveImg.visibility = View.VISIBLE
                        }

                        // Handle save and unsave actions directly inside onBindViewHolder
                        SaveImageView.setOnClickListener {
                            val updatedFavorites = currentFavorites.toMutableList()
                            if (!updatedFavorites.contains(meal.idMeal.toString())) {
                                updatedFavorites.add(meal.idMeal.toString())
                                updateFavorites(currentUser.uid, updatedFavorites)
                            }
                        }

                        SavedImageView.setOnClickListener {
                            val updatedFavorites = currentFavorites.toMutableList()
                            if (updatedFavorites.contains(meal.idMeal.toString())) {
                                updatedFavorites.remove(meal.idMeal.toString())
                                updateFavorites(currentUser.uid, updatedFavorites)
                            }
                        }

                    } else {
                        Log.e(TAG, "Failed to fetch current favorites: ${task.exception?.message}")
                        Toast.makeText(holder.itemView.context, "Failed to fetch favorites", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun updateFavorites(userId: String, updatedFavorites: List<String>) {
        database.child("users").child(userId).child("favorites").setValue(updatedFavorites)
            .addOnCompleteListener { saveTask ->
                if (saveTask.isSuccessful) {
                    Log.d(TAG, "Meal ID saved to favorites")
                    notifyDataSetChanged() // Notify RecyclerView of data set change after update
                } else {
                    Log.e(TAG, "Failed to save meal ID: ${saveTask.exception?.message}")
                }
            }

    }

}