package com.pbl.mobile.api.refresh_token

import android.app.Application
import com.pbl.mobile.model.remote.refresh.RefreshRequest
import com.pbl.mobile.model.remote.refresh.RefreshResponse

class RefreshTokenRequestManager {
    fun refresh(application: Application, request: RefreshRequest): RefreshResponse {
        return RefreshTokenApi.getApi(application).refresh(request)
    }
}
