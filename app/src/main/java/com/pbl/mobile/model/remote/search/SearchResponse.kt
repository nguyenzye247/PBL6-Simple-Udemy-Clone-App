package com.pbl.mobile.model.remote.search

import com.google.gson.annotations.SerializedName
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.model.local.Pagination

data class SearchResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: List<Course>,
    @SerializedName("pagination")
    val pagination: Pagination
)
