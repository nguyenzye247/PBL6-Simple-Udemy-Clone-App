package com.pbl.mobile.model.remote.video

import com.pbl.mobile.model.local.Video

data class GetVideoResponse(

    val status: String,

    val data: List<Video>
)
