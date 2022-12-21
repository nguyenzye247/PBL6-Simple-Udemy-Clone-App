package com.pbl.mobile.api.like

import android.app.Application
import com.pbl.mobile.model.remote.like.GetVideoLikeResponse
import com.pbl.mobile.model.remote.like.PostVideoLikeResponse
import com.pbl.mobile.model.remote.like.request.PostLikeRequestBody
import io.reactivex.rxjava3.core.Single

class VideoLikeRequestManager {
    fun getVideoLike(application: Application, videoId: String): Single<GetVideoLikeResponse> {
        return VideoLikeApi.getApi(application).getVideoLike(videoId)
    }

    fun likeVideo(
        application: Application,
        likeBody: PostLikeRequestBody
    ): Single<PostVideoLikeResponse> {
        return VideoLikeApi.getApi(application).postVideoLike(likeBody)
    }

    fun checkLikeVideo(
        application: Application,
        videoId: String,
        userId: String
    ): Single<PostVideoLikeResponse> {
        return VideoLikeApi.getApi(application).checkUserLikeVideo(videoId, userId)
    }
}
