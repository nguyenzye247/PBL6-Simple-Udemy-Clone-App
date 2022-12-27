package com.pbl.mobile.model.remote.video.view

import com.google.gson.annotations.SerializedName

data class UpdateViewBody(
    @SerializedName("lastDuration")
    val lastDuration: Float,
    @SerializedName("lastestViewDate")
    val lastestViewDate: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("videoId")
    val videoId: String
)
