package com.pbl.mobile.extension

import android.util.Patterns

internal fun CharSequence?.isEmailValid(): Boolean {
    return !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}