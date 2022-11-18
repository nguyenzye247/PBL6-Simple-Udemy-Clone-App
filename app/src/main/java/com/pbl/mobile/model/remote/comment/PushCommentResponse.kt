package com.pbl.mobile.model.remote.comment

import com.google.gson.annotations.SerializedName
import com.pbl.mobile.model.local.Comment

data class PushCommentResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val comment: Comment
)
