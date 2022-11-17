package com.pbl.mobile.model.remote.category.post

import com.google.gson.annotations.SerializedName

data class CategoryCreateRequest(
    @SerializedName("name")
    val name: String
)
