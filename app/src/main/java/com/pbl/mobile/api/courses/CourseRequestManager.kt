package com.pbl.mobile.api.courses

import android.app.Application
import com.pbl.mobile.model.remote.courses.GetCourseResponse
import com.pbl.mobile.model.remote.courses.GetCoursesResponse
import com.pbl.mobile.model.remote.courses.GetInstructorCourseResponse
import io.reactivex.rxjava3.core.Single

class CourseRequestManager {
    fun getCourses(application: Application, page: Int, limit: Int): Single<GetCoursesResponse> {
        return CoursesApi.getApi(application).getCourses(page, limit)
    }

    fun getCourseById(application: Application, courseId: String): Single<GetCourseResponse> {
        return CoursesApi.getApi(application).getCourseById(courseId)
    }

    fun getInstructorCourses(application: Application, userId: String): Single<GetInstructorCourseResponse> {
        return CoursesApi.getApi(application).getInstructorCourses(userId)
    }
}
