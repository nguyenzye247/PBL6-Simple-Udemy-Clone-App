package com.pbl.mobile.extension

import android.view.View
import android.widget.TextView
import androidx.core.text.isDigitsOnly

fun View.setVisibility(visible: Boolean, invisible: Int = View.GONE) {
    visibility = if (visible) View.VISIBLE else invisible
}

fun TextView.isEmpty(): Boolean {
    return this.text.isEmpty()
}

fun TextView.isNumber(): Boolean {
    return this.text != "" && this.text.isDigitsOnly()
}
