package com.pbl.mobile.util

import android.os.Build

object AppUtils {
    fun isAtleastNVersion() =
        Build.VERSION.SDK_INT > Build.VERSION_CODES.N

    fun isAtleasetMVersion() =
        Build.VERSION.SDK_INT > Build.VERSION_CODES.M
}