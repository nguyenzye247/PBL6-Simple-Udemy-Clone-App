package com.pbl.mobile.model.remote.hashtag

import com.google.gson.annotations.SerializedName
import com.pbl.mobile.model.local.Category
import com.pbl.mobile.model.local.Pagination

data class GetHashtagResponse(

    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val hashtags: List<Category>,
    @SerializedName("pagination")
    val pagination: Pagination?
)
