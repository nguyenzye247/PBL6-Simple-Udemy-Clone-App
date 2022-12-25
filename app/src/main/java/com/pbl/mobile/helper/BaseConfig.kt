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

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    var fullName: String
        get() = prefs.getString(KEY_USER_FULL_NAME, EMPTY_TEXT) ?: EMPTY_TEXT
        set(name) = prefs.edit().putString(KEY_USER_FULL_NAME, name).apply()

    var myEmail: String
        get() = prefs.getString(KEY_USER_EMAIL, EMPTY_TEXT) ?: EMPTY_TEXT
        set(name) = prefs.edit().putString(KEY_USER_EMAIL, name).apply()

    var myId: String
        get() = prefs.getString(KEY_MY_ID, EMPTY_TEXT) ?: EMPTY_TEXT
        set(id) = prefs.edit().putString(KEY_MY_ID, id).apply()

    var myAvatar: String
        get() = prefs.getString(KEY_USER_AVATAR, EMPTY_TEXT) ?: EMPTY_TEXT
        set(avatarUrl) = prefs.edit().putString(KEY_USER_AVATAR, avatarUrl).apply()

    var myRole: String
        get() = prefs.getString(KEY_USER_ROLE, EMPTY_TEXT) ?: EMPTY_TEXT
        set(role) = prefs.edit().putString(KEY_USER_ROLE, role).apply()

    var joinAt: String
        get() = prefs.getString(KEY_USER_JOIN_AT, EMPTY_TEXT) ?: EMPTY_TEXT
        set(time) = prefs.edit().putString(KEY_USER_JOIN_AT, time).apply()

    var token: String
        get() = prefs.getString(KEY_TOKEN, EMPTY_TEXT) ?: EMPTY_TEXT
        set(token) = prefs.edit().putString(KEY_TOKEN, token).apply()

    var refreshToken: String
        get() = prefs.getString(KEY_REFRESH_TOKEN, EMPTY_TEXT) ?: EMPTY_TEXT
        set(refreshToken) = prefs.edit().putString(KEY_REFRESH_TOKEN, refreshToken).apply()

    var expiresTime: Long
        get() = prefs.getLong(KEY_EXPIRES_TIME, 0)
        set(expireIn) = prefs.edit().putLong(KEY_EXPIRES_TIME, expireIn).apply()

    var initialLoginTime: Long
        get() = prefs.getLong(KEY_INIT_LOGIN_TIME, 0)
        set(time) = prefs.edit().putLong(KEY_INIT_LOGIN_TIME, time).apply()

    var isActivated: Boolean
        get() = prefs.getBoolean(KEY_IS_ACCOUNT_ACTIVATED, false)
        set(isActivated) = prefs.edit().putBoolean(KEY_IS_ACCOUNT_ACTIVATED, isActivated).apply()

    var role: String
        get() = prefs.getString(KEY_ROLE, Build.MODEL) ?: Build.MODEL
        set(role) = prefs.edit().putString(KEY_ROLE, role).apply()
}