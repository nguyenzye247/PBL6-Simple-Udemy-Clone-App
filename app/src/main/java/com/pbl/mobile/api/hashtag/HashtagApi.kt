package com.pbl.mobile.api.hashtag

import android.app.Application
import com.pbl.mobile.api.BaseRequestManager
import com.pbl.mobile.api.GET_HASHTAG_URL
import com.pbl.mobile.model.remote.hashtag.GetHashtagResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface HashtagApi {
    companion object {
        fun getApi(application: Application): HashtagApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(HashtagApi::class.java)
        }
    }

    @GET(GET_HASHTAG_URL)
    fun getAll(@Query("paging") paging: Int = 100): Single<GetHashtagResponse>
}
