package com.example.foodapp.Data

import androidx.lifecycle.MutableLiveData

data class User(
    var UserName: String? = null,
    var Email: String? = null,
    var ListFavorites: MutableList<String>? = null
)
