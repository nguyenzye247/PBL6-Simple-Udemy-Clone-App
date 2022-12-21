package com.pbl.mobile.model.remote.user

import com.google.gson.annotations.SerializedName

data class GetSimpleUserResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: User,
) {
    data class User(
        @SerializedName("email")
        val email: String,
        @SerializedName("fullName")
        val fullName: String,
        @SerializedName("role")
        val role: String,
        @SerializedName("userId")
        val userId: String,
        @SerializedName("avatarUrl")
        val avatarUrl: String,


    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other?.javaClass != javaClass) return false
            other as User
            return this.userId == other.userId
        }

        override fun hashCode(): Int {
            var result = email.hashCode()
            result = 31 * result + fullName.hashCode()
            result = 31 * result + role.hashCode()
            result = 31 * result + userId.hashCode()
            result = 31 * result + avatarUrl.hashCode()
            return result
        }
    }
}
