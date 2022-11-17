package com.pbl.mobile.model.remote.section

import com.google.gson.annotations.SerializedName
import com.pbl.mobile.model.local.Section

data class GetSectionsResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("sections")
    val sections: List<Section>,
)
