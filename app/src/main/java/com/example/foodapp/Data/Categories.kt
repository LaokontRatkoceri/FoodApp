package com.example.foodapp.Data

import com.google.gson.annotations.SerializedName

data class CategoriesList(
    @SerializedName("categories") var categories : ArrayList<Categories> = arrayListOf()

)


data class Categories (

    @SerializedName("idCategory") var idCategory             : String? = null,
    @SerializedName("strCategory") var strCategory            : String? = null,
    @SerializedName("strCategoryThumb") var strCategoryThumb       : String? = null,
    @SerializedName("strCategoryDescription") var strCategoryDescription : String? = null

)