package com.example.foodapp.ui.Home

import android.content.ContentValues.TAG
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.Data.Meals
import com.example.foodapp.Data.User
import com.example.foodapp.Data.mealsL
import com.example.foodapp.Data.mealsR
import com.example.foodapp.Utils.CircularTransformation
import com.example.foodapp.databinding.FoodItemBinding
import com.example.foodapp.databinding.ItemCategoryBinding
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.database
import com.squareup.picasso.Picasso

class MealsAdapter(val onItemClick: (Meals) -> Unit):RecyclerView.Adapter<MealsAdapter.ViewHolder>(){


    private lateinit var auth: FirebaseAuth

    private lateinit var database: DatabaseReference
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

//    override fun onBindViewHolder(holder: MealsAdapter.ViewHolder, position: Int) {
//        val meal = meals[position]
//
//        auth = Firebase.auth
//        database = Firebase.database.reference
//
//        with(holder.binding) {
//            MealName.text = meal.strMeal.toString()
//            Picasso.get().load(meal.strMealThumb).transform(CircularTransformation()).into(FoodImage)
//
//            root.setOnClickListener {
//                onItemClick(meal)
//            }
//
//            val user = auth.currentUser

//            user?.let {
//                val userRef = database.child("users").child(it.uid).child("favorites")
//
//                // Fetch the current list of favorite meals
//                userRef.get().addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        val snapshot = task.result
//                        val currentFavorites = snapshot.getValue(object : GenericTypeIndicator<List<String>>() {}) ?: emptyList()
//
//                        // Check if the current meal is already in favorites
//                        if (currentFavorites.contains(meal.idMeal.toString())) {
////                             Meal is already in favorites
//                            SaveImageView.visibility = View.GONE
//                            SavedImageView.visibility = View.VISIBLE
//                            savedImg.visibility = View.VISIBLE
//                            saveImg.visibility = View.GONE
//                        } else {
//                            // Meal is not in favorites, allow saving
//                            SaveImageView.setOnClickListener {
//                                // Add the meal ID to the list of favorite meals
//                                val updatedFavorites = currentFavorites.toMutableList()
//                                updatedFavorites.add(meal.idMeal.toString())
//
//                                // Save the updated list back to Firebase
//                                userRef.setValue(updatedFavorites).addOnCompleteListener { saveTask ->
//                                    if (saveTask.isSuccessful) {
//                                        Log.d(TAG, "Meal ID saved to favorites")
//                                        Toast.makeText(holder.itemView.context, "Added to favorites", Toast.LENGTH_SHORT).show()
//                                        SaveImageView.visibility = View.GONE
//                                        SavedImageView.visibility = View.VISIBLE
//                                        savedImg.visibility = View.VISIBLE
//                                        saveImg.visibility = View.GONE
//                                    } else {
//                                        Log.e(TAG, "Failed to save meal ID: ${saveTask.exception?.message}")
//                                        Toast.makeText(holder.itemView.context, "Failed to add to favorites", Toast.LENGTH_SHORT).show()
//                                    }
//                                }
//                            }
//
//
//                        }
//                    } else {
//                        Log.e(TAG, "Failed to fetch current favorites: ${task.exception?.message}")
//                        Toast.makeText(holder.itemView.context, "Failed to fetch favorites", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
//    }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val meal = meals[position]
                auth = Firebase.auth
                database = Firebase.database.reference

                with(holder.binding) {
                    MealName.text = meal.strMeal.toString()
                    Picasso.get().load(meal.strMealThumb).transform(CircularTransformation()).into(FoodImage)

                    root.setOnClickListener {
                        onItemClick(meal)
                    }

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



    override fun getItemCount(): Int = meals.size

}