package com.pbl.mobile.extension

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.setError(isError: Boolean, errorText: String?) {
    this.isErrorEnabled = isError
    this.error = errorText
}
