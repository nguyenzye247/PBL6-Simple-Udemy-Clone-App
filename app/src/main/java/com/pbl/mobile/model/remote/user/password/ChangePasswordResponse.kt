package com.pbl.mobile.model.remote.user.password

import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("message")
        val message: String
    )
}
