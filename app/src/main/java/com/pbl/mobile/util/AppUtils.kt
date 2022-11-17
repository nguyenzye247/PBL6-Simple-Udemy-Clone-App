package com.pbl.mobile.util

import android.os.Build

object AppUtils {
    fun isGreaterThan_N_Version() =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
}