package com.pbl.mobile.model.local

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    var id: String,
    @SerializedName("full_name")
    var fullName: String,
    @SerializedName("avatar_url")
    var avatarUrl: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("confirmation_token")
    var confirmationToken: String,
    @SerializedName("confirm_at")
    var confirmAt: Long,
    @SerializedName("is_actived")
    var isActive: Boolean,
    @SerializedName("password")
    var password: String,
    @SerializedName("verify_code")
    val verifyCode: String,
    @SerializedName("verify_code_send_at")
    var verifyCodeSendAt: Long,
    @SerializedName("role")
    var role: String,
    @SerializedName("created_at")
    var createdAt: Long,
    @SerializedName("deleted_at")
    var deletedAt: Long,
    @SerializedName("updated_at")
    var updatedAt: Long
)