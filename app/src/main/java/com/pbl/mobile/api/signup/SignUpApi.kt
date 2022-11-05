package com.pbl.mobile.api.signup

import android.app.Application
import com.pbl.mobile.api.BaseRequestManager
import com.pbl.mobile.api.SIGN_IN_URL
import com.pbl.mobile.api.SIGN_UP_URL
import com.pbl.mobile.model.remote.signup.SignUpRequest
import com.pbl.mobile.model.remote.signup.SignUpResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SignUpApi {
    companion object {
        fun getApi(application: Application): SignUpApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(SignUpApi::class.java)
        }
    }

    @POST(SIGN_UP_URL)
    fun register(
        @Header("Authorization") token: String,
        @Body request: SignUpRequest
    ): Single<SignUpResponse>
}