package com.pbl.mobile.model.remote.category.post

import com.google.gson.annotations.SerializedName
import com.pbl.mobile.model.local.Category

data class CategoryCreateResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val category: Category
)
