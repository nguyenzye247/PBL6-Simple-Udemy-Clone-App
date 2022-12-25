package com.pbl.mobile.model.remote.user

import com.google.gson.annotations.SerializedName

data class UpdateUserAvatarBody(
    @SerializedName("avatarUrl")
    val avatarUrl: String
)
