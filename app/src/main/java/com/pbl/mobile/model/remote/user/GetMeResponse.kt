package com.pbl.mobile.model.remote.user

import com.google.gson.annotations.SerializedName

data class GetMeResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("email")
        val email: String,
        @SerializedName("fullName")
        val fullName: String,
        @SerializedName("role")
        val role: String,
        @SerializedName("userId")
        val userId: String,
        @SerializedName("avatarUrl")
        val avatarUrl: String?
    )
}
