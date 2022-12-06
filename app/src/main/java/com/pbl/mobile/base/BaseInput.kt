package com.pbl.mobile.base

import android.app.Application
import com.pbl.mobile.model.local.Course

sealed class BaseInput {
    object NoInput : BaseInput()

    data class MainInput(
        val application: Application
    ) : BaseInput()

    data class CourseDetailInput(
        val application: Application
    ) : BaseInput()
}
