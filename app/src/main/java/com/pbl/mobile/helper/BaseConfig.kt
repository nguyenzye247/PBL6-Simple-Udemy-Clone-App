package com.pbl.mobile.helper

import android.content.Context
import android.os.Build
import com.pbl.mobile.common.KEY_REFRESH_TOKEN
import com.pbl.mobile.common.KEY_TOKEN
import com.pbl.mobile.common.KEY_USER_NAME

open class BaseConfig(pContext: Context) {
    companion object {
        const val PREFS_KEY = "prefs_key"
        fun newInstance(context: Context) = BaseConfig(context)
    }

    private val prefs = pContext.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

    var userName: String
        get() = prefs.getString(KEY_USER_NAME, Build.MODEL) ?: Build.MODEL
        set(name) = prefs.edit().putString(KEY_USER_NAME, name).apply()

    var token: String
        get() = prefs.getString(KEY_TOKEN, Build.MODEL) ?: Build.MODEL
        set(token) = prefs.edit().putString(KEY_TOKEN, token).apply()

    var refreshToken: String
        get() = prefs.getString(KEY_REFRESH_TOKEN, Build.MODEL) ?: Build.MODEL
        set(refreshToken) = prefs.edit().putString(KEY_REFRESH_TOKEN, refreshToken).apply()
}