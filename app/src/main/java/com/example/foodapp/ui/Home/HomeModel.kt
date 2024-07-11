package com.example.foodapp.ui.Home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.Data.Categories
import com.example.foodapp.Data.CategoriesList
import com.example.foodapp.Data.Meals
import com.example.foodapp.Data.MealsList
import com.example.foodapp.Data.mealsL
import com.example.foodapp.Data.mealsR
import com.example.foodapp.domain.Repository
import retrofit2.Call
import retrofit2.Response


class HomeModel: ViewModel() {


    val categoriesList = MutableLiveData<List<Categories>>()
    val mealsList = MutableLiveData<List<Meals>>()
    val mealsList1 = MutableLiveData<List<mealsR>>()
    val meal : MutableLiveData<mealsR> = MutableLiveData()



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

    fun getFoodByCategories(category: String){
        repository.service.getFoodByCategories(category).enqueue(object : retrofit2.Callback<MealsList>{
            override fun onResponse(call: Call<MealsList>, response: Response<MealsList>) {
                mealsList.postValue(response.body()!!.meals)
            }

            override fun onFailure(call: Call<MealsList>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    fun getRandom(){
        repository.service.getRandom().enqueue(object : retrofit2.Callback<mealsL>{
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
}