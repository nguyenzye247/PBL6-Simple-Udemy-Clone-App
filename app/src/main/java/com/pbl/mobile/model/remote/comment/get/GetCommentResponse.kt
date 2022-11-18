package com.pbl.mobile.model.remote.comment.get

import com.google.gson.annotations.SerializedName
import com.pbl.mobile.model.local.Comment
import com.pbl.mobile.model.local.Pagination

data class GetCommentResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val comments: List<Comment>,
    @SerializedName("pagination")
    val pagination: Pagination
)
