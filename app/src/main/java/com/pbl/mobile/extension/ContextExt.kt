package com.pbl.mobile.extension

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.pbl.mobile.helper.BaseConfig

fun Context?.showToast(mes: String, duration: Int = Toast.LENGTH_SHORT) {
    this?.let {
        Toast.makeText(this, mes, duration).show()
    }
}

fun Context?.showToast(@StringRes mes: Int, duration: Int = Toast.LENGTH_SHORT) {
    this?.let {
        Toast.makeText(this, mes, duration).show()
    }
}

fun Context.getBaseConfig() : BaseConfig {
    return  BaseConfig.newInstance(this)
}