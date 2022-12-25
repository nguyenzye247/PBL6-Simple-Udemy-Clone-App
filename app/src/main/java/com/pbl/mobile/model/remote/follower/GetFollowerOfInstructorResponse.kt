package com.pbl.mobile.model.remote.follower

import com.google.gson.annotations.SerializedName
import com.pbl.mobile.model.local.Pagination

data class GetFollowerOfInstructorResponse(
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
        @SerializedName("createdAt")
        val createdAt: String,
        @SerializedName("userResponse")
        val userData: UserData,
        @SerializedName("instructorData")
        val instructorData: InstructorData?
    )

    data class UserData(
        @SerializedName("userId")
        val userId: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("fullName")
        val fullName: String,
        @SerializedName("role")
        val role: String,
        @SerializedName("avatarUrl")
        val avatarUrl: String
    )

    data class InstructorData(
        val instructorId: String,
    )
}
