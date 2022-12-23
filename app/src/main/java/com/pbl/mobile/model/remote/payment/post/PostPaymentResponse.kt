package com.pbl.mobile.model.remote.payment.post

import com.google.gson.annotations.SerializedName

data class PostPaymentResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: Data,
) {
    data class Data(
        @SerializedName("id")
        val id: String,
        @SerializedName("vnpOrderInfo")
        val orderInfo: String,
        @SerializedName("orderType")
        val orderType: String,
        @SerializedName("amount")
        val amount: Double,
        @SerializedName("locate")
        val locate: String,
        @SerializedName("ipAddress")
        val ipAddress: String,
        @SerializedName("paymentUrl")
        val paymentUrl: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("txnRef")
        val txnRef: String,
        @SerializedName("timeOver")
        val timeOver: String,
        @SerializedName("userId")
        val userId: String,
        @SerializedName("courseId")
        val courseId: String,
        @SerializedName("instructorId")
        val instructorId: String,
        @SerializedName("bankCode")
        val bankCode: String?,
        @SerializedName("bankTransactionNo")
        val bankTransactionNo: String?,
        @SerializedName("cardType")
        val cardType: String?,
        @SerializedName("createdAt")
        val createdAt: String,
        @SerializedName("updatedAt")
        val updatedAt: String,
        @SerializedName("deletedAt")
        val deletedAt: String?
    )
}
