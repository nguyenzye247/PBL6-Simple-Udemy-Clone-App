package com.pbl.mobile.api.instructor

import android.app.Application
import com.pbl.mobile.api.BaseRequestManager
import com.pbl.mobile.api.GET_INSTRUCTORS_REQUEST_URL
import com.pbl.mobile.api.GET_INSTRUCTORS_STUDENTS_URL
import com.pbl.mobile.api.PAYMENT_URL
import com.pbl.mobile.model.remote.instructor.GetInstructorStudentsResponse
import com.pbl.mobile.model.remote.instructor.PostInstructorRequestResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface InstructorApi {
    companion object {
        fun getApi(application: Application): InstructorApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(InstructorApi::class.java)
        }
    }

    @POST(GET_INSTRUCTORS_REQUEST_URL)
    fun requestToBecomeInstructor(): Single<PostInstructorRequestResponse>

    @GET("$GET_INSTRUCTORS_STUDENTS_URL/{instructorId}/$PAYMENT_URL")
    fun getInstructorStudents(
        @Path("instructorId") instructorId: String,
        @Query("page") page: Int = 1,
        @Query("paging") paging: Int = 1
    ): Single<GetInstructorStudentsResponse>
}
