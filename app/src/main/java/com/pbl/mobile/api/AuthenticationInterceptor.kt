package com.pbl.mobile.api

import android.app.Application
import android.content.Intent
import com.pbl.mobile.api.refresh.RefreshApi
import com.pbl.mobile.api.refresh.RefreshRequestManager
import com.pbl.mobile.extension.getBaseConfig
import com.pbl.mobile.ui.signin.SignInActivity
import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor(
    private val application: Application
) : Interceptor {
    private val refreshRequestManager = RefreshRequestManager()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = application.getBaseConfig().token
        val refreshToken = application.getBaseConfig().refreshToken

        val newRequest = request.newBuilder()
            .header("Authorization", BEARER + accessToken)
            .build()
        val response = chain.proceed(newRequest)

        if (response.code == 401) {
            refreshAccessToken(refreshToken)?.let { newAccessToken ->
                application.getBaseConfig().token = newAccessToken

                val anotherNewRequest = request.newBuilder()
                    .header("Authorization", BEARER + newAccessToken)
                    .build()
                return chain.proceed(anotherNewRequest)
            }
        }

        return response
    }

    private fun refreshAccessToken(refreshToken: String): String? {
        // Make a request to the refresh token endpoint to get a new access token
        // Return the new access token if the request is successful, or null if it fails
        val response =
            refreshRequestManager.refreshToken(application, RefreshApi.RefreshBody(refreshToken))
                .execute()
        return if (response.isSuccessful) {
            response.body()?.data?.token
        } else if (response.code() == 400 || response.code() == 401) {
            logout()
            null
        } else {
            null
        }
    }

    private fun logout() {
        application.getBaseConfig().clearAll()
        application.startActivity(
            Intent(application, SignInActivity::class.java)
        )
    }
}