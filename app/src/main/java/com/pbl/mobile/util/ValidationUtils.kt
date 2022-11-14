package com.pbl.mobile.util

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.pbl.mobile.extension.setError

object ValidationUtils {

    fun isUploadInputValid(
        editText: EditText,
        inputLayout: TextInputLayout,
        errorText: String?
    ): Boolean {
        if (editText.text.isNotEmpty()) {
            inputLayout.setError(false, null)
            return true
        }
        inputLayout.setError(true, errorText)
        return false
    }

}
