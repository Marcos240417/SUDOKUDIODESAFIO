package com.marcos.mymoovieappapi.data.remote.response

import com.google.gson.annotations.SerializedName
import com.marcos.mymoovieappapi.data.remote.model.MovieResult

data class MovieResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<MovieResult>,
    @SerializedName("total_pages")
    val total_pages: Int,
    @SerializedName("total_results")
    val total_results: Int
)
