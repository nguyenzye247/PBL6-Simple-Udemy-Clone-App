package com.pbl.mobile.api.refresh_token

import android.app.Application
import com.pbl.mobile.api.BaseRequestManager
import com.pbl.mobile.api.REFRESH_URL
import com.pbl.mobile.model.remote.refresh.RefreshRequest
import com.pbl.mobile.model.remote.refresh.RefreshResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenApi {
    companion object {
        fun getApi(application: Application): RefreshTokenApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(RefreshTokenApi::class.java)
        }
    }

    @POST(REFRESH_URL)
    fun refresh(@Body request: RefreshRequest): RefreshResponse
}
