package com.pbl.mobile.model.local

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("createdAt")
    val createdAt: Long,
    @SerializedName("updatedAt")
    val updatedAt: Long?,
    @SerializedName("deletedAt")
    val deletedAt: Long?,
    @SerializedName("id")
    val id: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("videoId")
    val videoId: String
)
