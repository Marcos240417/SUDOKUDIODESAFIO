package com.marcos.mymoovieappapi.data.remote.response

import com.google.gson.annotations.SerializedName
import com.marcos.mymoovieappapi.data.remote.model.SearchResult

data class SearchResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<SearchResult>,
    @SerializedName("total_pages")
    val total_pages: Int,
    @SerializedName("total_results")
    val total_results: Int
)
