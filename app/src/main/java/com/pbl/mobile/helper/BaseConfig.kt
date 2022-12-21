package com.pbl.mobile.helper

import android.content.Context
import android.os.Build
import com.pbl.mobile.common.*

open class BaseConfig(pContext: Context) {
    companion object {
        const val PREFS_KEY = "prefs_key"
        fun newInstance(context: Context) = BaseConfig(context)
    }

    private val prefs = pContext.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

    var userName: String
        get() = prefs.getString(KEY_USER_NAME, Build.MODEL) ?: Build.MODEL
        set(name) = prefs.edit().putString(KEY_USER_NAME, name).apply()

    var myId: String
        get() = prefs.getString(KEY_MY_ID, Build.MODEL) ?: Build.MODEL
        set(id) = prefs.edit().putString(KEY_MY_ID, id).apply()

    var myAvatar: String
        get() = prefs.getString(KEY_USER_AVATAR, Build.MODEL) ?: Build.MODEL
        set(avatarUrl) = prefs.edit().putString(KEY_USER_AVATAR, avatarUrl).apply()

    var myRole: String
        get() = prefs.getString(KEY_USER_ROLE, Build.MODEL) ?: Build.MODEL
        set(role) = prefs.edit().putString(KEY_USER_AVATAR, role).apply()

    var token: String
        get() = prefs.getString(KEY_TOKEN, Build.MODEL) ?: Build.MODEL
        set(token) = prefs.edit().putString(KEY_TOKEN, token).apply()

    var refreshToken: String
        get() = prefs.getString(KEY_REFRESH_TOKEN, Build.MODEL) ?: Build.MODEL
        set(refreshToken) = prefs.edit().putString(KEY_REFRESH_TOKEN, refreshToken).apply()

    var expiresTime: Long
        get() = prefs.getLong(KEY_EXPIRES_TIME, 0)
        set(expireIn) = prefs.edit().putLong(KEY_EXPIRES_TIME, expireIn).apply()

    var isActivated: Boolean
        get() = prefs.getBoolean(KEY_IS_ACCOUNT_ACTIVATED, false)
        set(isActivated) = prefs.edit().putBoolean(KEY_IS_ACCOUNT_ACTIVATED, isActivated).apply()

    var role: String
        get() = prefs.getString(KEY_ROLE, Build.MODEL) ?: Build.MODEL
        set(role) = prefs.edit().putString(KEY_ROLE, role).apply()
}