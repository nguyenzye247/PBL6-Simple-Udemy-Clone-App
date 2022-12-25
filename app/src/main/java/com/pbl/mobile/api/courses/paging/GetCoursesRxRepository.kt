package com.pbl.mobile.api.courses.paging

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.pbl.mobile.api.courses.paging.source.CoursePagingSource
import com.pbl.mobile.api.courses.paging.source.InstructorCoursePagingSource
import com.pbl.mobile.model.local.Course

interface GetCoursesRxRepository {
    fun getCourses(coursePagingSource: CoursePagingSource): LiveData<PagingData<Course>>

    fun getInstructorCourses(instructorCoursePagingSource: InstructorCoursePagingSource): LiveData<PagingData<Course>>
}
