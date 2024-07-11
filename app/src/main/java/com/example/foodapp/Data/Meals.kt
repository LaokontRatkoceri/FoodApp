package com.example.foodapp.Data

import com.google.gson.annotations.SerializedName


data class MealsList (

    @SerializedName("meals" ) var meals : ArrayList<Meals> = arrayListOf()

)

data class Meals (

    @SerializedName("strMeal"      ) var strMeal      : String? = null,
    @SerializedName("strMealThumb" ) var strMealThumb : String? = null,
    @SerializedName("idMeal"       ) var idMeal       : String? = null

)