package com.pbl.mobile.model.remote.instructor

import com.google.gson.annotations.SerializedName
import com.pbl.mobile.model.local.Pagination

data class GetInstructorStudentsResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("pagination")
    val pagination: Pagination
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
        val instructorId: String
    )
}
