package com.pbl.mobile.model.remote.video.view

import com.google.gson.annotations.SerializedName

data class UpdateViewResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("id")
        val id: String,
        @SerializedName("countView")
        val countView: Int,
        @SerializedName("lastDuration")
        val lastDuration: Float,
        @SerializedName("highestDuration")
        val highestDuration: Float,
        @SerializedName("lastestViewDate")
        val lastestViewDate: String,
        @SerializedName("createdAt")
        val createdAt: String,
        @SerializedName("updatedAt")
        val updatedAt: String,
        @SerializedName("deletedAt")
        val deletedAt: String?,
        @SerializedName("userId")
        val userId: String,
        @SerializedName("videoId")
        val videoId: String
    )
}
