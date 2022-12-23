package com.pbl.mobile.model.remote.courses

import com.google.gson.annotations.SerializedName
import com.pbl.mobile.model.local.Course

data class GetCourseResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val course: Course
)
