package com.pbl.mobile.api.courses

import android.app.Application
import com.pbl.mobile.api.BaseRequestManager
import com.pbl.mobile.api.GET_COURSES_URL
import com.pbl.mobile.model.remote.courses.GetCourseResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoursesApi {
    companion object {
        fun getApi(application: Application): CoursesApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(CoursesApi::class.java)
        }
    }

    @GET(GET_COURSES_URL)
    fun getCourses(@Query("page") page: Int, @Query("limit") limit: Int): Single<GetCourseResponse>
}
