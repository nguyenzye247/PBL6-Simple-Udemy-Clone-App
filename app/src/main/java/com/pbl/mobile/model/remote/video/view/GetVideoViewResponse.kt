package com.pbl.mobile.model.remote.video.view

import com.google.gson.annotations.SerializedName

data class GetVideoViewResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: Data,
) {
    data class Data(
        @SerializedName("id")
        val id : String,
        @SerializedName("countView")
        val countView: Long,
        @SerializedName("lastDuration")
        val lastDuration: Double,
        @SerializedName("lastedViewDate")
        val lastedViewDate: String,
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
