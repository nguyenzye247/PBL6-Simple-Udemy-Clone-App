package com.pbl.mobile.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.api.category.CategoryRequestManager
import com.pbl.mobile.api.courses.CourseRequestManager
import com.pbl.mobile.api.hashtag.HashtagRequestManager
import com.pbl.mobile.api.payment.PaymentRequestManager
import com.pbl.mobile.api.search.SearchRequestManager
import com.pbl.mobile.api.search.paging.SearchCoursePagingSource
import com.pbl.mobile.api.search.paging.SearchRxRepositoryImpl
import com.pbl.mobile.api.user.UserRequestManager
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.BaseViewModel
import com.pbl.mobile.extension.getBaseConfig
import com.pbl.mobile.extension.observeOnUiThread
import com.pbl.mobile.model.dto.FilterData
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.model.remote.category.get.CategoryGetResponse
import com.pbl.mobile.model.remote.hashtag.GetHashtagResponse
import com.pbl.mobile.model.remote.user.GetSimpleUserResponse
import io.sentry.Sentry
import kotlinx.coroutines.flow.Flow

class SearchViewModel(val input: BaseInput.SearchInput) : BaseViewModel(input) {
    private val categoryRequestManager = CategoryRequestManager()
    private val hashtagRequestManager = HashtagRequestManager()
    private val courseRequestManager = CourseRequestManager()
    private val paymentRequestManager = PaymentRequestManager()
    private val userRequestManager = UserRequestManager()
    private val searchRequestManager = SearchRequestManager()
    private val searchCourseRxRepositoryImpl = SearchRxRepositoryImpl()

    private val _categories: MutableLiveData<BaseResponse<CategoryGetResponse>> = MutableLiveData()
    private val _hashtags: MutableLiveData<BaseResponse<GetHashtagResponse>> = MutableLiveData()
    private val _isFinishedLoadSearchInstructor: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _isFinishedLoadMyPurchaseCourses: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        getCategories()
        getHashtags()
    }

    fun categories(): LiveData<BaseResponse<CategoryGetResponse>> = _categories
    fun hashtag(): LiveData<BaseResponse<GetHashtagResponse>> = _hashtags
    fun isFinishedLoadSearchInstructor(): LiveData<Boolean> = _isFinishedLoadSearchInstructor
    fun isFinishedLoadMyPurchaseCourses(): LiveData<Boolean> = _isFinishedLoadMyPurchaseCourses

    val homeInstructors: ArrayList<GetSimpleUserResponse.User> = arrayListOf()
    val myPurchaseCourses: ArrayList<Course> = arrayListOf()

    fun searchCourse(
        key: String,
        minPrice: Long, maxPrice: Long,
        category: String,
        tag: String
    ): Flow<PagingData<Course>> = searchCourseRxRepositoryImpl.search(
        SearchCoursePagingSource(
            input.application,
            searchRequestManager,
            key,
            minPrice, maxPrice,
            category,
            tag
        )
    )

    private fun getCategories() {
        try {
            addDisposables(
                categoryRequestManager.getCategories(input.application)
                    .observeOnUiThread()
                    .doOnSuccess {
                        _categories.value = BaseResponse.Loading()
                    }
                    .subscribe(
                        { categoryResponse ->
                            _categories.value = BaseResponse.Success(categoryResponse)
                        },
                        { throwable ->
                            Sentry.captureException(throwable)
                        }
                    )
            )
        } catch (ex: Exception) {
            _categories.value = BaseResponse.Error(ex.message)
        }
    }

    private fun getHashtags() {
        try {
            addDisposables(
                hashtagRequestManager.getHashtags(input.application)
                    .observeOnUiThread()
                    .doOnSuccess {
                        _hashtags.value = BaseResponse.Loading()
                    }
                    .subscribe(
                        { hashtagResponse ->
                            _hashtags.value = BaseResponse.Success(hashtagResponse)
                        },
                        { throwable ->
                            _hashtags.value = BaseResponse.Error(throwable.message)
                            Sentry.captureException(throwable)
                        }
                    )
            )
        } catch (ex: Exception) {
            _hashtags.value = BaseResponse.Error(ex.message)
        }
    }

    fun loadSearchInstructors(userIds: Set<String>) {
        userIds.forEach { userId ->
            subscription.add(
                userRequestManager.getSimpleUserById(input.application, userId)
                    .observeOnUiThread()
                    .subscribe(
                        { simpleUserResponse ->
                            if (!homeInstructors.contains(simpleUserResponse.data)) {
                                homeInstructors.add(simpleUserResponse.data)
                                _isFinishedLoadSearchInstructor.value =
                                    homeInstructors.size == userIds.size
                            }
                        },
                        { throwable ->

                        }
                    )
            )
        }
    }

    fun loadMyPurchasedCourseIds() {
        val userIds = input.application.getBaseConfig().myId
        subscription.add(
            paymentRequestManager.getPayment(
                input.application,
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
                    courseRequestManager.getCourseById(input.application, courseId)
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
}
