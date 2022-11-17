package com.pbl.mobile.api.courses.paging

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.pbl.mobile.model.local.Course

interface GetCoursesRxRepository {
    fun getCourses(): LiveData<PagingData<Course>>
}
