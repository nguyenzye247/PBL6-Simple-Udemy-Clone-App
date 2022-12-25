package com.pbl.mobile.api.instructor

import android.app.Application
import com.pbl.mobile.model.remote.instructor.GetInstructorStudentsResponse
import com.pbl.mobile.model.remote.instructor.PostInstructorRequestResponse
import io.reactivex.rxjava3.core.Single

class InstructorRequestManager {
    fun getInstructorStudents(application: Application, instructorId: String): Single<GetInstructorStudentsResponse> {
        return InstructorApi.getApi(application).getInstructorStudents(instructorId)
    }

    fun requestToBecomeInstructor(application: Application): Single<PostInstructorRequestResponse> {
        return InstructorApi.getApi(application).requestToBecomeInstructor()
    }
}
