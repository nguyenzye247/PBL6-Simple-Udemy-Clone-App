package com.pbl.mobile.base

import android.app.Application
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.model.local.Lecture

sealed class BaseInput {
    object NoInput : BaseInput()

    data class MainInput(
        val application: Application
    ) : BaseInput()

    data class CourseDetailInput(
        val application: Application
    ) : BaseInput()

    data class WatchLectureInput(
        val application: Application,
        val lecture: Lecture?
    ) : BaseInput()
}
