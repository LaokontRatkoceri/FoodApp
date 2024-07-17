package com.example.foodapp.domain


import com.example.foodapp.Data.CategoriesList
import com.example.foodapp.Data.MealsList
import com.example.foodapp.Data.mealsL
import com.example.foodapp.Data.mealsR
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("categories.php")
    fun getAllCategories(): Call<CategoriesList>

    @GET("filter.php")
    fun getFoodByCategories(@Query("c") category: String): Call<MealsList>

    @GET("random.php")
    fun getRandom(): Call<mealsL>

    @GET("lookup.php")
    fun getFoodById(@Query("i") mealId : String): Call<mealsL>

    @GET("lookup.php")
    fun getFavoriteById(@Query("i") mealId : String): Call<mealsL>

    @GET("lookup.php")
    fun getSearchedItemByName(@Query("s") mealName : String): Call<mealsL>

}