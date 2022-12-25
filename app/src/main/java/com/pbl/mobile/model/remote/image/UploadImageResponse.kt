package com.pbl.mobile.model.remote.image

import com.google.gson.annotations.SerializedName

data class UploadImageResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("id")
        val id: String,
        @SerializedName("url")
        val imageUrl: String
    )
}
