package com.pbl.mobile.util

import android.os.Build

object VersionChecker {

    fun isAndroid_M_AndAbove() : Boolean{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }
}