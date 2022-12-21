package com.pbl.mobile.api

import android.content.Context
import com.pbl.mobile.extension.getBaseConfig

object SessionManager {

    fun saveToken(pContext: Context, token: String) {
        pContext.getBaseConfig().token = token
    }

    fun fetchToken(pContext: Context) = pContext.getBaseConfig().token

    fun saveRefreshToken(pContext: Context, refreshToken: String) {
        pContext.getBaseConfig().refreshToken = refreshToken
    }

    fun fetchRefreshToken(pContext: Context) = pContext.getBaseConfig().refreshToken

    fun saveExpiresTime(pContext: Context, expiresDuration: String) {
        try {
            val expiresDurationInMilli = expiresDuration.toLong()
            pContext.getBaseConfig().expiresTime =
                System.currentTimeMillis() + expiresDurationInMilli
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun fetchExpiresTime(pContext: Context) = pContext.getBaseConfig().expiresTime

    fun clearData(pContext: Context) {
        pContext.getBaseConfig().token = ""
        pContext.getBaseConfig().refreshToken = ""
    }
}
