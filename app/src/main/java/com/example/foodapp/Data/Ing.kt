package com.example.foodapp.Data

import com.google.gson.annotations.SerializedName

data class ings (

    @SerializedName("meals" ) var meals : ArrayList<Ing> = arrayListOf()

)

data class Ing(
    @SerializedName("idIngredient"   ) var idIngredient   : String? = null,
    @SerializedName("strIngredient"  ) var strIngredient  : String? = null,
    @SerializedName("strDescription" ) var strDescription : String? = null,
    @SerializedName("strType"        ) var strType        : String? = null
)