package com.pbl.mobile.api.signin

import android.app.Application
import com.pbl.mobile.model.remote.signin.SignInRequest
import com.pbl.mobile.model.remote.signin.SignInResponse
import io.reactivex.rxjava3.core.Single

class SignInRequestManager {
    fun login(application: Application, token: String, request: SignInRequest): Single<SignInResponse> {
        return SignInAPI.getApi(application).login(token, request)
    }
}
