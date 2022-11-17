package com.pbl.mobile.ui.main

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.pbl.mobile.api.courses.CourseRequestManager
import com.pbl.mobile.api.courses.GetCoursesRxRepositoryImpl
import com.pbl.mobile.api.courses.paging.CoursePagingSource
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.BaseViewModel
import com.pbl.mobile.model.local.Course

class HomeMainViewModel(private val input: BaseInput.MainInput) : BaseViewModel(input) {
    private val pApplication = input.application
    private val courseRequestManager = CourseRequestManager()
    private val getCoursesRxRepositoryImpl = GetCoursesRxRepositoryImpl(
        CoursePagingSource(
            pApplication,
            courseRequestManager
        )
    )

    fun getCourses(): LiveData<PagingData<Course>> = getCoursesRxRepositoryImpl.getCourses()
}
