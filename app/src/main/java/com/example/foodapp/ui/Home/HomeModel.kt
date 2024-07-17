package com.example.foodapp.ui.Home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.Data.Categories
import com.example.foodapp.Data.CategoriesList
import com.example.foodapp.Data.Meals
import com.example.foodapp.Data.MealsList
import com.example.foodapp.Data.mealsL
import com.example.foodapp.Data.mealsR
import com.example.foodapp.domain.Repository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicInteger


class HomeModel: ViewModel() {


    val categoriesList = MutableLiveData<List<Categories>>()
    val mealsList = MutableLiveData<List<Meals>>()
    val mealsList1 = MutableLiveData<List<mealsR>>()
    val meal: MutableLiveData<mealsR> = MutableLiveData()
    val mealL1: MutableLiveData<mealsL> = MutableLiveData()
    val favoriteMeals = MutableLiveData<MutableList<mealsR>>().apply { value = mutableListOf() }

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val repository = Repository()

    init {
        getCategories()
        getRandom()

    }




    fun getCategories() {
        repository.service.getAllCategories().enqueue(object : retrofit2.Callback<CategoriesList> {
            override fun onResponse(
                call: Call<CategoriesList>,
                response: Response<CategoriesList>
            ) {
                categoriesList.postValue(response.body()!!.categories)
            }

            override fun onFailure(call: Call<CategoriesList>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getFoodByCategories(category: String) {
        repository.service.getFoodByCategories(category)
            .enqueue(object : retrofit2.Callback<MealsList> {
                override fun onResponse(call: Call<MealsList>, response: Response<MealsList>) {
                    mealsList.postValue(response.body()!!.meals)
                }

                override fun onFailure(call: Call<MealsList>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }

    fun getRandom() {
        repository.service.getRandom().enqueue(object : retrofit2.Callback<mealsL> {
            override fun onResponse(call: Call<mealsL>, response: Response<mealsL>) {
                mealsList1.postValue(response.body()!!.meals1)
            }

            override fun onFailure(call: Call<mealsL>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    fun getFoodById(id: String) {
        repository.service.getFoodById(id).enqueue(object : retrofit2.Callback<mealsL> {
            override fun onResponse(call: Call<mealsL>, response: Response<mealsL>) {
                if (response.isSuccessful) {
                    val mealData = response.body()
                    if (mealData != null) {
                        meal.postValue(mealData.meals1[0])
                        Log.d("ViewModel", "Meal data received successfully: $mealData")
                    } else {
                        Log.w("ViewModel", "Response body is null for meal ID: $id")
                        // Handle case where response body is unexpectedly null
                    }
                } else {
                    Log.e(
                        "ViewModel",
                        "Failed to fetch meal. Response: ${response.code()}, ${response.message()}"
                    )
                    // Handle other cases of unsuccessful response (e.g., HTTP error codes)
                }
            }


            override fun onFailure(call: Call<mealsL>, t: Throwable) {
                t.printStackTrace()  // Print the stack trace of the error
                // Handle the failure case, e.g., update UI to show error message
            }
        })
    }


    fun getFavoriteById(id: String) {
        repository.service.getFavoriteById(id).enqueue(object : retrofit2.Callback<mealsL> {
            override fun onResponse(call: Call<mealsL>, response: Response<mealsL>) {
                if (response.isSuccessful) {
                    response.body()?.let { mealL ->
                        val newMeals = mealL.meals1
                        val currentFavorites = favoriteMeals.value ?: mutableListOf()
                        currentFavorites.addAll(newMeals)
                        favoriteMeals.postValue(currentFavorites)
                    }
                } else {
                    Log.e(
                        "ViewModel",
                        "Failed to fetch meal. Response: ${response.code()}, ${response.message()}"
                    )
                }
            }
            override fun onFailure(call: Call<mealsL>, t: Throwable) {
                t.printStackTrace()  // Print the stack trace of the error
                // Handle the failure case, e.g., update UI to show error message
            }
        })
    }

    fun clearFavoriteMeals() {
        favoriteMeals.value?.clear()
        favoriteMeals.postValue(favoriteMeals.value)
    }
}