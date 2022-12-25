package com.pbl.mobile.model.remote.user

import com.google.gson.annotations.SerializedName

data class UpdateUserProfileResponse(
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
