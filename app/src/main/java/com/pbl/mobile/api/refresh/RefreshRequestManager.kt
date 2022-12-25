package com.pbl.mobile.api.refresh

import android.app.Application
import com.pbl.mobile.model.remote.signin.SignInResponse
import retrofit2.Call

class RefreshRequestManager {
    fun refreshToken(
        application: Application,
        refreshBody: RefreshApi.RefreshBody
    ): Call<SignInResponse> {
        return RefreshApi.getApi(application).refreshToken(refreshBody)
    }
}
