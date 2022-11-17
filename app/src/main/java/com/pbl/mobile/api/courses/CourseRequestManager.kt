package com.pbl.mobile.api.courses

import android.app.Application
import com.pbl.mobile.model.remote.courses.GetCourseResponse
import io.reactivex.rxjava3.core.Single

class CourseRequestManager {
    fun getCourses(application: Application, page: Int, limit: Int): Single<GetCourseResponse> {
        return CoursesApi.getApi(application).getCourses(page, limit)
    }

    fun getInstructorCourses(application: Application, id: String, page: Int, limit: Int): Single<GetCourseResponse> {
        return CoursesApi.getApi(application).getInstructorCourses(id, page, limit)
    }
}
