package com.pbl.mobile.model.local

import com.google.gson.annotations.SerializedName

data class Section(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("deletedAt")
    val deletedAt: String?,
    @SerializedName("courseId")
    val courseId: String
)
