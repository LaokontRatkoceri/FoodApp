package com.example.foodapp.ui.Home

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeModel: ViewModel() {

    // LiveData për listat e kategorive dhe ushqimeve
    val categoriesList = MutableLiveData<List<Categories>>()
    val mealsList = MutableLiveData<List<Meals>>()
    private val _meal = MutableLiveData<mealsR?>()
    val mealR: LiveData<mealsR?> get() = _meal

    val mealsList1 = MutableLiveData<List<mealsR>>()
    val meal: MutableLiveData<mealsR> = MutableLiveData()
    val mealL1: MutableLiveData<mealsL> = MutableLiveData()
    val favoriteMeals = MutableLiveData<MutableList<mealsR>>().apply { value = mutableListOf() }

    // Referenca për Firebase Database dhe Firebase Auth
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Referenca për Repository
    private val repository = Repository()

    // Inicializimi i metodave për të marrë të dhënat
    init {
        getCategories()
        getRandom()
    }

    // Metoda për të marrë kategoritë e ushqimeve nga shërbimi i rrjetit
    fun getCategories() {
        repository.service.getAllCategories().enqueue(object : retrofit2.Callback<CategoriesList> {
            override fun onResponse(call: Call<CategoriesList>, response: Response<CategoriesList>) {
                categoriesList.postValue(response.body()!!.categories)
            }

            override fun onFailure(call: Call<CategoriesList>, t: Throwable) {
                t.printStackTrace() // Printon stack trace në rast dështimi
            }
        })
    }

    // Metoda për të marrë ushqimet sipas kategorisë nga shërbimi i rrjetit
    fun getFoodByCategories(category: String) {
        repository.service.getFoodByCategories(category)
            .enqueue(object : retrofit2.Callback<MealsList> {
                override fun onResponse(call: Call<MealsList>, response: Response<MealsList>) {
                    mealsList.postValue(response.body()!!.meals)
                }

                override fun onFailure(call: Call<MealsList>, t: Throwable) {
                    t.printStackTrace() // Printon stack trace në rast dështimi
                }
            })
    }

    // Metoda për të marrë ushqime rastësore nga shërbimi i rrjetit
    fun getRandom() {
        repository.service.getRandom().enqueue(object : retrofit2.Callback<mealsL> {
            override fun onResponse(call: Call<mealsL>, response: Response<mealsL>) {
                mealsList1.postValue(response.body()!!.meals1)
            }

            override fun onFailure(call: Call<mealsL>, t: Throwable) {
                t.printStackTrace() // Printon stack trace në rast dështimi
            }
        })
    }

    // Metoda për të marrë ushqimin sipas ID-së nga shërbimi i rrjetit
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
                        // Trajto rastin kur trupi i përgjigjes është null
                    }
                } else {
                    Log.e("ViewModel", "Failed to fetch meal. Response: ${response.code()}, ${response.message()}")
                    // Trajto rastet e përgjigjes jo të suksesshme (p.sh., kodet e gabimeve HTTP)
                }
            }

            override fun onFailure(call: Call<mealsL>, t: Throwable) {
                t.printStackTrace() // Printon stack trace në rast dështimi
                // Trajto rastin e dështimit, p.sh., përditëso UI-n për të treguar mesazhin e gabimit
            }
        })
    }

    // Metoda për të marrë ushqimet e preferuara sipas ID-së nga shërbimi i rrjetit
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
                    Log.e("ViewModel", "Failed to fetch meal. Response: ${response.code()}, ${response.message()}")
                }
            }

            override fun onFailure(call: Call<mealsL>, t: Throwable) {
                t.printStackTrace() // Printon stack trace në rast dështimi
                // Trajto rastin e dështimit, p.sh., përditëso UI-n për të treguar mesazhin e gabimit
            }
        })
    }

    // Metoda për të pastruar listën e ushqimeve të preferuara
    fun clearFavoriteMeals() {
        favoriteMeals.value?.clear()
        favoriteMeals.postValue(favoriteMeals.value)
    }

}
