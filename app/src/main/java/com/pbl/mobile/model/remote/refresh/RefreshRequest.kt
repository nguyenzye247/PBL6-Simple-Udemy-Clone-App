package com.pbl.mobile.model.remote.refresh

import com.google.gson.annotations.SerializedName

data class RefreshRequest(
    @SerializedName("refreshToken")
    var refreshToken: String
)
