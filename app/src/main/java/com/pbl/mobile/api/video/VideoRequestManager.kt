package com.pbl.mobile.api.video

import android.app.Application
import com.pbl.mobile.model.remote.video.GetVideoResponse
import com.pbl.mobile.model.remote.video.view.GetVideoViewResponse
import com.pbl.mobile.model.remote.video.view.UpdateViewBody
import com.pbl.mobile.model.remote.video.view.UpdateViewResponse
import io.reactivex.rxjava3.core.Single

class VideoRequestManager {
    fun getSectionVideos(application: Application, sectionId: String): Single<GetVideoResponse> {
        return VideoApi.getApi(application).getSectionVideos(sectionId)
    }

    fun getVideoViews(
        application: Application,
        userId: String,
        videoId: String
    ): Single<GetVideoViewResponse> {
        return VideoApi.getApi(application).getVideoViews(userId, videoId)
    }

    fun updateVideoView(
        application: Application,
        videoViewBody: UpdateViewBody
    ): Single<UpdateViewResponse> {
        return VideoApi.getApi(application).putVideoView(videoViewBody)
    }
}
