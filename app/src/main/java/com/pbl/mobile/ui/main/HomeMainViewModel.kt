package com.pbl.mobile.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.api.category.CategoryRequestManager
import com.pbl.mobile.api.courses.CourseRequestManager
import com.pbl.mobile.api.courses.paging.GetCoursesRxRepositoryImpl
import com.pbl.mobile.api.courses.paging.source.CoursePagingSource
import com.pbl.mobile.api.courses.paging.source.InstructorCoursePagingSource
import com.pbl.mobile.api.follower.FollowerRequestManager
import com.pbl.mobile.api.instructor.InstructorRequestManager
import com.pbl.mobile.api.payment.PaymentRequestManager
import com.pbl.mobile.api.user.UserRequestManager
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.BaseViewModel
import com.pbl.mobile.extension.getBaseConfig
import com.pbl.mobile.extension.observeOnUiThread
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.model.remote.category.get.CategoryGetResponse
import com.pbl.mobile.model.remote.follower.GetFollowerOfInstructorResponse
import com.pbl.mobile.model.remote.instructor.GetInstructorStudentsResponse
import com.pbl.mobile.model.remote.instructor.PostInstructorRequestResponse
import com.pbl.mobile.model.remote.user.GetSimpleUserResponse
import io.sentry.Sentry

class HomeMainViewModel(private val input: BaseInput.MainInput) : BaseViewModel(input) {
    private val pApplication = input.application
    private val courseRequestManager = CourseRequestManager()
    private val userRequestManager = UserRequestManager()
    private val instructorRequestManager = InstructorRequestManager()
    private val categoryRequestManager = CategoryRequestManager()
    private val paymentRequestManager = PaymentRequestManager()
    private val followerRequestManager = FollowerRequestManager()
    private val getCoursesRxRepositoryImpl = GetCoursesRxRepositoryImpl()

    private val _isFinishedLoadHomeInstructor: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _isFinishedLoadPurchasedInstructor: MutableLiveData<Boolean> =
        MutableLiveData(false)
    private val _isFinishedLoadMyPurchaseCourses: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _categories: MutableLiveData<BaseResponse<CategoryGetResponse>> = MutableLiveData()
    private val _instructorStudents: MutableLiveData<BaseResponse<GetInstructorStudentsResponse>> =
        MutableLiveData()
    private val _instructorFollowersResponse: MutableLiveData<BaseResponse<GetFollowerOfInstructorResponse>> =
        MutableLiveData()
    private val _requestInstructorResponse: MutableLiveData<BaseResponse<PostInstructorRequestResponse>> =
        MutableLiveData()

    val homeInstructors: ArrayList<GetSimpleUserResponse.User> = arrayListOf()
    val purchasedInstructors: ArrayList<GetSimpleUserResponse.User> = arrayListOf()
    val myPurchaseCourses: ArrayList<Course> = arrayListOf()

    fun getCourses(): LiveData<PagingData<Course>> = getCoursesRxRepositoryImpl.getCourses(
        CoursePagingSource(
            pApplication,
            courseRequestManager
        )
    )

    fun getInstructorCourses(): LiveData<PagingData<Course>> =
        getCoursesRxRepositoryImpl.getInstructorCourses(
            InstructorCoursePagingSource(
                pApplication,
                pApplication.getBaseConfig().myId,
                courseRequestManager
            )
        )

    fun categories(): LiveData<BaseResponse<CategoryGetResponse>> = _categories
    fun isFinishedLoadHomeInstructor(): LiveData<Boolean> = _isFinishedLoadHomeInstructor
    fun isFinishedLoadPurchasedInstructor(): LiveData<Boolean> = _isFinishedLoadPurchasedInstructor
    fun isFinishedLoadMyPurchaseCourses(): LiveData<Boolean> = _isFinishedLoadMyPurchaseCourses
    fun instructorStudents(): LiveData<BaseResponse<GetInstructorStudentsResponse>> =
        _instructorStudents

    fun requestInstructorResponse(): LiveData<BaseResponse<PostInstructorRequestResponse>> =
        _requestInstructorResponse

    fun getCategories() {
        try {
            addDisposables(
                categoryRequestManager.getCategories(input.application)
                    .observeOnUiThread()
                    .subscribe(
                        { categoryResponse ->
                            _categories.value = BaseResponse.Success(categoryResponse)
                        },
                        { throwable ->
                            //TODO: handle errors
                            Sentry.captureException(throwable)
                        }
                    )
            )
        } catch (ex: Exception) {
            _categories.value = BaseResponse.Error(ex.message)
        }
    }

    fun loadInstructorStudents() {
        subscription.add(
            instructorRequestManager.getInstructorStudents(
                pApplication,
                pApplication.getBaseConfig().myId
            )
                .observeOnUiThread()
                .subscribe(
                    { instructorStudentResponse ->
                        _instructorStudents.value = BaseResponse.Success(instructorStudentResponse)
                    },
                    { throwable ->
                        _instructorStudents.value = BaseResponse.Error(throwable.message)
                    }
                )
        )
    }

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
                        myPurchaseCourses.clear()
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
                                if (courseIds.size == myPurchaseCourses.size)
                                    _isFinishedLoadMyPurchaseCourses.value = true
                            },
                            { throwable ->
                                throwable.printStackTrace()
                            }
                        )
                )
            }
        }
    }

    fun getInstructorFollowers(): LiveData<BaseResponse<GetFollowerOfInstructorResponse>> =
        _instructorFollowersResponse

    fun loadInstructorFollowers() {
        subscription.add(
            followerRequestManager.getFollowersOfInstructor(
                pApplication,
                pApplication.getBaseConfig().myId,
                1,
                1
            )
                .observeOnUiThread()
                .subscribe(
                    { followerResponse ->
                        _instructorFollowersResponse.value = BaseResponse.Success(followerResponse)
                    },
                    { throwable ->
                        _instructorFollowersResponse.value = BaseResponse.Error(throwable.message)
                    }
                )
        )
    }

    fun requestToBecomeInstructor() {
        subscription.add(
            instructorRequestManager.requestToBecomeInstructor(pApplication)
                .observeOnUiThread()
                .subscribe(
                    { requestInstructorResponse ->
                        _requestInstructorResponse.value =
                            BaseResponse.Success(requestInstructorResponse)
                    },
                    { throwable ->
                        _requestInstructorResponse.value = BaseResponse.Error(throwable.message)
                    }
                )
        )
    }
}
