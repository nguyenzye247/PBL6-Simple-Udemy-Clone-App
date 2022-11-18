package com.pbl.mobile.model.remote.comment

import com.google.gson.annotations.SerializedName

data class PushCommentRequest(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("videoId")
    val videoId: String,
)
