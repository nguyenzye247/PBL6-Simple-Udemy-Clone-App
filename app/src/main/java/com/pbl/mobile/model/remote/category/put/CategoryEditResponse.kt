package com.pbl.mobile.model.remote.category.put

import com.google.gson.annotations.SerializedName
import com.pbl.mobile.model.local.Category

data class CategoryEditResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val category: Category
)
