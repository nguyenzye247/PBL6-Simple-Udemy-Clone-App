package com.pbl.mobile.extension

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import androidx.core.view.updateLayoutParams

fun View.setVisibility(visible: Boolean, invisible: Int = View.GONE) {
    visibility = if (visible) View.VISIBLE else invisible
}

fun View.setMarginTop(value: Int) = updateLayoutParams<ViewGroup.MarginLayoutParams> {
    topMargin = value
}

fun View.setMarginBottom(value: Int) = updateLayoutParams<ViewGroup.MarginLayoutParams> {
    bottomMargin = value
}

fun TextView.isEmpty(): Boolean {
    return this.text.isEmpty()
}

fun TextView.isNumber(): Boolean {
    return this.text != "" && this.text.isDigitsOnly()
}
