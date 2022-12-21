package com.pbl.mobile.model.remote.like

import com.google.gson.annotations.SerializedName

data class PostVideoLikeResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: Data?
) {
    data class Data(
        @SerializedName("id")
        val id: String,
        @SerializedName("userId")
        val userId: String,
        @SerializedName("videoId")
        val videoId: String,
        @SerializedName("createdAt")
        val createdAt: String,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("deletedAt")
        val deletedAt: String?,
        @SerializedName("like")
        val isLike: Boolean
    )
}
