package com.pbl.mobile.model.remote.signup

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("fullName")
    var fullName: String,
)