package com.pbl.mobile.model.remote.signup

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("status")
    var status: String,
    @SerializedName("data")
    var data: SignUpData
) {
    data class SignUpData(
        @SerializedName("email")
        var email: String,
        @SerializedName("role")
        var role: String,
        @SerializedName("fullName")
        var fullName: String,
        @SerializedName("isActivated")
        var isActivated: Boolean
    )
}
