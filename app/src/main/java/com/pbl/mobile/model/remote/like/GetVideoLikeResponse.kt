package com.pbl.mobile.model.remote.like

import com.google.gson.annotations.SerializedName

data class GetVideoLikeResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: List<Data>
) {
    data class Data(
        @SerializedName("createdAt")
        val createdAt: String,
        @SerializedName("updatedAt")
        val updatedAt: String,
        @SerializedName("deletedAt")
        val deletedAt: String?,
        @SerializedName("id")
        val id: String,
        @SerializedName("userId")
        val userId: String,
        @SerializedName("videoId")
        val videoId: String,
        @SerializedName("like")
        val isLike: Boolean,
    )
}
