package com.pbl.mobile.api.video

import android.app.Application
import com.pbl.mobile.model.remote.video.GetVideoResponse
import io.reactivex.rxjava3.core.Single

class VideoRequestManager {
    fun getSectionVideos(application: Application, sectionId: String): Single<GetVideoResponse> {
        return VideoApi.getApi(application).getSectionVideos(sectionId)
    }
}
