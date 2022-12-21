package com.pbl.mobile.api.user

import android.app.Application
import com.pbl.mobile.api.BaseRequestManager
import com.pbl.mobile.api.GET_ME
import com.pbl.mobile.api.GET_USER
import com.pbl.mobile.model.remote.user.GetMeResponse
import com.pbl.mobile.model.remote.user.GetSimpleUserResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserApi {
    companion object {
        fun getApi(application: Application): UserApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(UserApi::class.java)
        }
    }

    @GET("$GET_USER/{ID}")
    fun getSimpleUserById(@Path("ID") userId: String): Single<GetSimpleUserResponse>

    @GET(GET_ME)
    fun getMe(
        @Header("Authorization") token: String
    ): Single<GetMeResponse>
}
