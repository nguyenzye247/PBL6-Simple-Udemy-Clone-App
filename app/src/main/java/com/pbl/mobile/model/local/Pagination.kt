package com.pbl.mobile.model.local

import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("prev_page")
    val prevPage: Int?,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("next_page")
    val next_page: Int?,
    @SerializedName("total_count")
    val totalCount: Int
)
