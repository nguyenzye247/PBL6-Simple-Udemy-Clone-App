package com.pbl.mobile.model.remote.category.get

import com.google.gson.annotations.SerializedName
import com.pbl.mobile.model.local.Category
import com.pbl.mobile.model.local.Pagination

data class CategoryGetResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val categories: List<Category>,
    @SerializedName("pagination")
    val pagination: Pagination
)
