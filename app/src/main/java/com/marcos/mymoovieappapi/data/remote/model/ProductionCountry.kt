package com.marcos.mymoovieappapi.data.remote.model

import com.google.gson.annotations.SerializedName

data class ProductionCountry(
    @SerializedName("iso_3166_1")
    val iso33661: String,
    @SerializedName("name")
    val name: String
)
