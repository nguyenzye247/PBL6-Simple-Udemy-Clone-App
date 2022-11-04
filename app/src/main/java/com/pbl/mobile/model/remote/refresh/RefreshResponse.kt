package com.pbl.mobile.model.remote.refresh

import com.google.gson.annotations.SerializedName

data class RefreshResponse(
    @SerializedName("token")
    var token: String,
    @SerializedName("refreshToken")
    var refreshToken: String
)
