package com.pbl.mobile.model.remote.payment.post

import com.google.gson.annotations.SerializedName

data class PostPaymentBody(
    @SerializedName("courseId")
    val courseId: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("orderInfo")
    val orderInfo: String,
    @SerializedName("ipAddress")
    val ipAddress: String,
)
