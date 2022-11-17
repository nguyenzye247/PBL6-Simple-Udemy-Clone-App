package com.pbl.mobile.model.local

import com.google.gson.annotations.SerializedName

data class Course(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("isActived")
    val isActivated: Boolean,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("deletedAt")
    val deletedAt: String?,
    @SerializedName("categoryTopicId")
    val categoryTopicId: String,
    @SerializedName("userId")
    val userId: String
)
