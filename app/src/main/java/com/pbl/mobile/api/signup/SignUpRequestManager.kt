package com.pbl.mobile.api.signup

import android.app.Application
import com.pbl.mobile.model.remote.signup.SignUpRequest
import com.pbl.mobile.model.remote.signup.SignUpResponse
import io.reactivex.rxjava3.core.Single

class SignUpRequestManager {
    fun register(
        application: Application,
        token: String,
        request: SignUpRequest
    ): Single<SignUpResponse> {
        return SignUpApi.getApi(application).register(token, request)
    }
}