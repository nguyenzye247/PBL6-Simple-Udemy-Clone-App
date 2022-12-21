package com.pbl.mobile.model.remote.like.request

import com.google.gson.annotations.SerializedName

data class PostLikeRequestBody(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("videoId")
    val videoId: String
)
