package com.pbl.mobile.api.video

import android.app.Application
import com.pbl.mobile.api.BaseRequestManager
import com.pbl.mobile.api.GET_SECTION_URL
import com.pbl.mobile.api.GET_SECTION_VIDEOS_URL
import com.pbl.mobile.model.remote.video.GetVideoResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface VideoApi {
    companion object {
        fun getApi(application: Application): VideoApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(VideoApi::class.java)
        }
    }

    @GET("$GET_SECTION_URL/{ID}/$GET_SECTION_VIDEOS_URL")
    fun getSectionVideos(@Path("ID") sectionId: String): Single<GetVideoResponse>
}
