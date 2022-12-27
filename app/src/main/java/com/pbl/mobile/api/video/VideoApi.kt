package com.pbl.mobile.api.video

import android.app.Application
import com.pbl.mobile.api.*
import com.pbl.mobile.model.remote.video.GetVideoResponse
import com.pbl.mobile.model.remote.video.view.GetVideoViewResponse
import com.pbl.mobile.model.remote.video.view.UpdateViewBody
import com.pbl.mobile.model.remote.video.view.UpdateViewResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface VideoApi {
    companion object {
        fun getApi(application: Application): VideoApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(VideoApi::class.java)
        }
    }

    @GET("$GET_SECTION_URL/{ID}/$VIDEOS_URL")
    fun getSectionVideos(@Path("ID") sectionId: String): Single<GetVideoResponse>

    @GET("$GET_USER/{userId}/$VIDEOS_URL/{videoId}/$VIDEO_VIEWS_URL")
    fun getVideoViews(
        @Path("userId") userId: String,
        @Path("videoId") videoId: String
    ): Single<GetVideoViewResponse>

    @PUT(UPDATE_VIDEO_VIEWS_URL)
    fun putVideoView(
        @Body videoViewBody: UpdateViewBody
    ): Single<UpdateViewResponse>
}
