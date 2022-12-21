package com.pbl.mobile.api.like

import android.app.Application
import com.pbl.mobile.api.*
import com.pbl.mobile.model.remote.like.GetVideoLikeResponse
import com.pbl.mobile.model.remote.like.PostVideoLikeResponse
import com.pbl.mobile.model.remote.like.request.PostLikeRequestBody
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface VideoLikeApi {
    companion object {
        fun getApi(application: Application): VideoLikeApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(VideoLikeApi::class.java)
        }
    }

    @GET("${GET_VIDEO_LIKE_URL}/{videoId}/${LIKE_REACT_URL}")
    fun getVideoLike(
        @Path("videoId") videoId: String
    ): Single<GetVideoLikeResponse>

    @POST(POST_VIDEO_LIKE_URL)
    fun postVideoLike(
        @Body likeBody: PostLikeRequestBody
    ): Single<PostVideoLikeResponse>

    @GET("${GET_CHECK_LIKE_URL}/{videoId}/users/{userId}/${LIKE_CHECK_URL}")
    fun checkUserLikeVideo(
        @Path("videoId") videoId: String,
        @Path("userId") userId: String
    ): Single<PostVideoLikeResponse>
}
