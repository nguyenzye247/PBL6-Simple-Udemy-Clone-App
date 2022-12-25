package com.pbl.mobile.util

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.pbl.mobile.extension.setError

object ValidationUtils {
    private const val PHONE_NUMBER_PATTERN = "(84|0[3|5|7|8|9])+([0-9]{8})\\b"

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

    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        val phoneRegex = PHONE_NUMBER_PATTERN.toRegex()
        val isMatch = phoneNumber.matches(phoneRegex)
        return isMatch && phoneNumber.isNotEmpty()
    }
}
