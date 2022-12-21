package com.pbl.mobile.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.pbl.mobile.api.courses.CourseRequestManager
import com.pbl.mobile.api.courses.paging.GetCoursesRxRepositoryImpl
import com.pbl.mobile.api.courses.paging.source.CoursePagingSource
import com.pbl.mobile.api.user.UserRequestManager
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.BaseViewModel
import com.pbl.mobile.extension.observeOnUiThread
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.model.remote.user.GetSimpleUserResponse

class HomeMainViewModel(private val input: BaseInput.MainInput) : BaseViewModel(input) {
    private val pApplication = input.application
    private val courseRequestManager = CourseRequestManager()
    private val userRequestManager = UserRequestManager()
    private val getCoursesRxRepositoryImpl = GetCoursesRxRepositoryImpl(
        CoursePagingSource(
            pApplication,
            courseRequestManager
        )
    )
    private val _isFinishedLoadInstructor: MutableLiveData<Boolean> = MutableLiveData(false)
    val instructors: ArrayList<GetSimpleUserResponse.User> = arrayListOf()

    fun loadInstructors(userIds: List<String>) {
        userIds.forEach { userId ->
            subscription.add(
                userRequestManager.getSimpleUserById(input.application, userId)
                    .observeOnUiThread()
                    .subscribe(
                        { simpleUserResponse ->
                            if (!instructors.contains(simpleUserResponse.data)) {
                                instructors.add(simpleUserResponse.data)
                                Log.d("INSTRUCTOR-123", "$instructors")
                                _isFinishedLoadInstructor.value = instructors.size == userIds.size
                            }
                        },
                        { throwable ->

                        }
                    )
            )
        }
    }

    fun getCourses(): LiveData<PagingData<Course>> = getCoursesRxRepositoryImpl.getCourses()

    fun isFinishedLoadInstructor(): LiveData<Boolean> = _isFinishedLoadInstructor
}
