package com.pbl.mobile.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.pbl.mobile.api.courses.CourseRequestManager
import com.pbl.mobile.api.courses.paging.GetCoursesRxRepositoryImpl
import com.pbl.mobile.api.courses.paging.source.CoursePagingSource
import com.pbl.mobile.api.payment.PaymentRequestManager
import com.pbl.mobile.api.user.UserRequestManager
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.BaseViewModel
import com.pbl.mobile.extension.getBaseConfig
import com.pbl.mobile.extension.observeOnUiThread
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.model.remote.user.GetSimpleUserResponse

class HomeMainViewModel(private val input: BaseInput.MainInput) : BaseViewModel(input) {
    private val pApplication = input.application
    private val courseRequestManager = CourseRequestManager()
    private val userRequestManager = UserRequestManager()
    private val paymentRequestManager = PaymentRequestManager()
    private val getCoursesRxRepositoryImpl = GetCoursesRxRepositoryImpl(
        CoursePagingSource(
            pApplication,
            courseRequestManager
        )
    )
    private val _isFinishedLoadHomeInstructor: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _isFinishedLoadPurchasedInstructor: MutableLiveData<Boolean> =
        MutableLiveData(false)
    private val _isFinishedLoadMyPurchaseCourses: MutableLiveData<Boolean> = MutableLiveData(false)
    val homeInstructors: ArrayList<GetSimpleUserResponse.User> = arrayListOf()
    val purchasedInstructors: ArrayList<GetSimpleUserResponse.User> = arrayListOf()
    val myPurchaseCourses: ArrayList<Course> = arrayListOf()

    fun getCourses(): LiveData<PagingData<Course>> = getCoursesRxRepositoryImpl.getCourses()
    fun isFinishedLoadHomeInstructor(): LiveData<Boolean> = _isFinishedLoadHomeInstructor
    fun isFinishedLoadPurchasedInstructor(): LiveData<Boolean> = _isFinishedLoadPurchasedInstructor
    fun isFinishedLoadMyPurchaseCourses(): LiveData<Boolean> = _isFinishedLoadMyPurchaseCourses

    fun loadHomeInstructors(userIds: Set<String>) {
        userIds.forEach { userId ->
            subscription.add(
                userRequestManager.getSimpleUserById(input.application, userId)
                    .observeOnUiThread()
                    .subscribe(
                        { simpleUserResponse ->
                            if (!homeInstructors.contains(simpleUserResponse.data)) {
                                homeInstructors.add(simpleUserResponse.data)
                                _isFinishedLoadHomeInstructor.value =
                                    homeInstructors.size == userIds.size
                            }
                        },
                        { throwable ->

                        }
                    )
            )
        }
    }

    fun loadMyPurchasedInstructor(userIds: Set<String>) {
        userIds.forEach { userId ->
            subscription.add(
                userRequestManager.getSimpleUserById(input.application, userId)
                    .observeOnUiThread()
                    .subscribe(
                        { simpleUserResponse ->
                            if (!purchasedInstructors.contains(simpleUserResponse.data)) {
                                purchasedInstructors.add(simpleUserResponse.data)
                                _isFinishedLoadPurchasedInstructor.value =
                                    purchasedInstructors.size == userIds.size
                            }
                        },
                        { throwable ->

                        }
                    )
            )
        }
    }

    fun loadMyPurchasedCourseIds() {
        val userIds = pApplication.getBaseConfig().myId
        subscription.add(
            paymentRequestManager.getPayment(
                pApplication,
                userIds
            )
                .observeOnUiThread()
                .doOnSubscribe {

                }
                .subscribe(
                    { paymentResponse ->
                        loadMyPurchasedCourses(paymentResponse.data.map {
                            it.courseId
                        })
                    },
                    {
                        //no-ops
                    }
                )
        )
    }

    private fun loadMyPurchasedCourses(courseIds: List<String>) {
        if (courseIds.isNotEmpty()) {
            courseIds.forEach { courseId ->
                subscription.add(
                    courseRequestManager.getCourseById(pApplication, courseId)
                        .observeOnUiThread()
                        .subscribe(
                            { courseResponse ->
                                myPurchaseCourses.add(courseResponse.course)
                                _isFinishedLoadMyPurchaseCourses.value =
                                    courseIds.size == myPurchaseCourses.size
                            },
                            { throwable ->
                                throwable.printStackTrace()
                            }
                        )
                )
            }
        }
    }
}
