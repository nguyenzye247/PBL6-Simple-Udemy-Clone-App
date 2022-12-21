package com.pbl.mobile.model.local

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("deletedAt")
    val deletedAt: String?,
    @SerializedName("id")
    val id: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("videoId")
    val videoId: String
)
