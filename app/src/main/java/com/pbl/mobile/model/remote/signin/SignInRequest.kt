package com.pbl.mobile.model.remote.signin

import com.google.gson.annotations.SerializedName

data class SignInRequest(
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String
)
