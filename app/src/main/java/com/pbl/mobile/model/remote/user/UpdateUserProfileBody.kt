package com.pbl.mobile.model.remote.user

import com.google.gson.annotations.SerializedName

data class UpdateUserProfileBody(
    @SerializedName("address")
    val address: String,
    @SerializedName("occupation")
    val occupation: String,
    @SerializedName("identityImageUrl")
    val identityImageUrl: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String
)
