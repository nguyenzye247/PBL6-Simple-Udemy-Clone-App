package com.pbl.mobile.model.remote.user

import com.google.gson.annotations.SerializedName

data class GetUserDetailResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("email")
        val email: String,
        @SerializedName("role")
        val role: String,
        @SerializedName("fullName")
        val fullName: String,
        @SerializedName("address")
        val address: String?,
        @SerializedName("isActivated")
        val isActivated: Boolean,
        @SerializedName("occupation")
        val occupation: String?,
        @SerializedName("phone")
        val phone: String?,
        @SerializedName("payment")
        val payment: String?,
        @SerializedName("dateOfBirth")
        val dateOfBirth: String,
        @SerializedName("identityImageUrl")
        val identityImageUrl: String?,
        @SerializedName("createdAt")
        val createdAt: String,
        @SerializedName("userId")
        val userId: String,
        @SerializedName("avatarUrl")
        val avatarUrl: String?
    )
}
