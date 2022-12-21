package com.pbl.mobile.model.remote.signin

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("status")
    var status: String,
    @SerializedName("data")
    var data: SignInData
) {
    data class SignInData(
        @SerializedName("token")
        var token: String,
        @SerializedName("refreshToken")
        var refreshToken: String,
        @SerializedName("expiresIn")
        val expiresIn: String,
        @SerializedName("type")
        val type: String
    )
}
