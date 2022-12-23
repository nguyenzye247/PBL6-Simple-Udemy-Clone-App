package com.pbl.mobile.model.remote.courses

import com.google.gson.annotations.SerializedName
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.model.local.Pagination

data class GetCoursesResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: List<Course>,
    @SerializedName("pagination")
    val pagination: Pagination
)
