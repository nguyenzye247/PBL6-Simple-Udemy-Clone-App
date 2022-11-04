package com.pbl.mobile.api.signin

import android.app.Application
import com.pbl.mobile.api.BaseRequestManager
import com.pbl.mobile.api.SIGN_IN_URL
import com.pbl.mobile.model.remote.signin.SignInRequest
import com.pbl.mobile.model.remote.signin.SignInResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SignInAPI {
    companion object {
        fun getApi(application: Application): SignInAPI {
            return BaseRequestManager.getInstance(application).myRetrofit.create(SignInAPI::class.java)
        }
    }

    @POST(SIGN_IN_URL)
    fun login(@Header("Authorization") token: String ,@Body request: SignInRequest): Single<SignInResponse>
}
