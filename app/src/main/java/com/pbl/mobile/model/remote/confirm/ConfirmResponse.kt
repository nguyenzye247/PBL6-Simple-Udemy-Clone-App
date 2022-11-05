package com.pbl.mobile.model.remote.confirm

import com.google.gson.annotations.SerializedName

data class ConfirmResponse(
    @SerializedName("status")
    var status: String,
    @SerializedName("data")
    var data: ConfirmData
) {
    data class ConfirmData(
        @SerializedName("token")
        var token: String,
        @SerializedName("refreshToken")
        var refreshToken: String
    )
}
