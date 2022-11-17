package com.pbl.mobile.model.remote.category.put

import com.google.gson.annotations.SerializedName

data class CategoryEditRequest(
    @SerializedName("name")
    val newName: String
)
