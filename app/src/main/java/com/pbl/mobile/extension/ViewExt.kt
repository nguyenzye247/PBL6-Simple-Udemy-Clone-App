package com.pbl.mobile.extension

import android.view.View
import android.widget.TextView

fun View.setVisibility(visible: Boolean, invisible: Int = View.GONE){
    visibility = if (visible) View.VISIBLE else invisible
}

fun TextView.isEmpty(): Boolean{
    return this.text == ""
}