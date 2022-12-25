package com.pbl.mobile.model.local

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lecture(
    @SerializedName("id")
    val id: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("duration")
    val duration: Double,
    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("isLock")
    val isLock: Boolean,
    @SerializedName("totalView")
    val totalView: Int,
    @SerializedName("totalComment")
    val totalComment: Int,
    @SerializedName("totalLike")
    val totalLike: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("deletedAt")
    val deletedAt: String?,
    @SerializedName("sectionId")
    val sectionId: String,
    @SerializedName("userId")
    val userId: String
) : Parcelable
