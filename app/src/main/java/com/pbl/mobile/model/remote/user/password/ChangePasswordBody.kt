package com.pbl.mobile.model.remote.user.password

import com.google.gson.annotations.SerializedName

data class ChangePasswordBody(
    @SerializedName("oldPassword")
    val oldPassword: String,
    @SerializedName("newPassword")
    val newPassword: String
)
