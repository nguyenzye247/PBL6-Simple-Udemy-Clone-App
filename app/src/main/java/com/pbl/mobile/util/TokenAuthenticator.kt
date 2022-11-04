package com.pbl.mobile.util

import android.app.Application
import com.pbl.mobile.api.SessionManager
import com.pbl.mobile.api.refresh_token.RefreshTokenRequestManager
import com.pbl.mobile.model.remote.refresh.RefreshRequest
import com.pbl.mobile.model.remote.refresh.RefreshResponse
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(val application: Application) : Authenticator {
    private val refreshTokenRequestManager = RefreshTokenRequestManager()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (SessionManager.fetchToken(application).isBlank())
            return null
        synchronized(this) {
            val newToken = getUpdatedToken()
            if (newToken.token.isNotBlank()) {
                newToken.let {
                    SessionManager.saveToken(application, it.token)
                    SessionManager.saveRefreshToken(application, it.refreshToken)
                    return response.request.newBuilder()
                        .header("Authorization", "Bearer ${it.token}")
                        .build()
                }
            }
        }
        return null
    }

    private fun getUpdatedToken(): RefreshResponse {
        val refreshToken = SessionManager.fetchRefreshToken(application)
        return refreshTokenRequestManager.refresh(application, RefreshRequest(refreshToken))
    }
}