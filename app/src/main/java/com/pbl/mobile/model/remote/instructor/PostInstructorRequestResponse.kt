package com.pbl.mobile.model.remote.instructor

import com.google.gson.annotations.SerializedName

data class PostInstructorRequestResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("id")
        val id: String,
        @SerializedName("userId")
        val userId: String,
        @SerializedName("reason")
        val reason: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("createdAt")
        val createdAt: String,
        @SerializedName("updatedAt")
        val updatedAt: String,
        @SerializedName("deletedAt")
        val deletedAt: String
    )
}
