package com.pbl.mobile.ui.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pbl.mobile.api.BEARER
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.api.SUCCESS
import com.pbl.mobile.api.SessionManager
import com.pbl.mobile.api.category.CategoryRequestManager
import com.pbl.mobile.api.section.SectionRequestManager
import com.pbl.mobile.api.video.VideoRequestManager
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.BaseViewModel
import com.pbl.mobile.extension.observeOnUiThread
import com.pbl.mobile.model.local.Lecture
import com.pbl.mobile.model.remote.category.get.CategoryGetResponse
import com.pbl.mobile.model.remote.section.GetSectionsResponse
import com.pbl.mobile.model.remote.video.GetVideoResponse

class CourseViewModel(val input: BaseInput.CourseDetailInput) : BaseViewModel(input) {
    private val sectionRequestManager = SectionRequestManager()
    private val categoryRequestManager = CategoryRequestManager()
    private val lectureRequestManager = VideoRequestManager()
    private val token = BEARER + SessionManager.fetchToken(input.application)

    private val _categories: MutableLiveData<BaseResponse<CategoryGetResponse>> = MutableLiveData()
    private val _courseSections: MutableLiveData<BaseResponse<GetSectionsResponse>> =
        MutableLiveData()
    private val _lectures: MutableLiveData<BaseResponse<GetVideoResponse>> = MutableLiveData()
    private val _isAllLecturesLoaded: MutableLiveData<Boolean> = MutableLiveData()
    private val sectionIds = arrayListOf<String>()
    private val sectionLectures = hashMapOf<String, ArrayList<Lecture>>()

    fun courseSections(): LiveData<BaseResponse<GetSectionsResponse>> = _courseSections
    fun categories(): LiveData<BaseResponse<CategoryGetResponse>> = _categories
    fun lectures() = sectionLectures
    fun isAllLectureLoaded(): LiveData<Boolean> = _isAllLecturesLoaded

    fun getCategories() {
        try {
            addDisposables(
                categoryRequestManager.getCategories(input.application)
                    .observeOnUiThread()
                    .subscribe(
                        { categoryResponse ->
                            handleCategoryResponse(categoryResponse)
                        },
                        { throwable ->
                            //TODO: handle errors
                        }
                    )
            )
        } catch (ex: Exception) {
            _categories.value = BaseResponse.Error(ex.message)
        }
    }

    fun getSections(courseId: String) {
        try {
            addDisposables(
                sectionRequestManager.getCourseSections(
                    input.application,
                    courseId
                )
                    .observeOnUiThread()
                    .doOnSubscribe {
                        _courseSections.value = BaseResponse.Loading()
                    }
                    .subscribe(
                        { sectionResponse ->
                            handleSectionResponse(sectionResponse)
                        },
                        { throwable ->
                            //TODO: handle errors
                        }
                    )
            )
        } catch (ex: Exception) {
            _courseSections.value = BaseResponse.Error(ex.message)
        }
    }

    private fun getLectures(sectionId: String) {
        try {
            addDisposables(
                lectureRequestManager.getSectionVideos(
                    input.application,
                    sectionId
                )
                    .observeOnUiThread()
                    .subscribe(
                        { lectureResponse ->
                            handleLectureResponse(sectionId, lectureResponse)
                        },
                        { throwable ->
                            //TODO: handle errors
                        }
                    )
            )
        } catch (ex: Exception) {
            _lectures.value = BaseResponse.Error(ex.message)
        }
    }

    private fun getAllCourseLectures() {
        if (sectionIds.isNotEmpty()) {
            sectionIds.forEach { sectionId ->
                getLectures(sectionId)
            }
        } else {
            _isAllLecturesLoaded.value = true
        }
    }

    private fun handleCategoryResponse(response: CategoryGetResponse) {
        if (response.status == SUCCESS)
            _categories.value = BaseResponse.Success(response)
        else
            _categories.value = BaseResponse.Error("Error loading categories")
    }

    private fun handleSectionResponse(response: GetSectionsResponse) {
        if (response.status == SUCCESS) {
            _courseSections.value = BaseResponse.Success(response)
            sectionIds.clear()
            sectionIds.addAll(response.data.sections.map { it.id })
            getAllCourseLectures()
        } else
            _courseSections.value = BaseResponse.Error("Error loading course details")
    }

    private fun handleLectureResponse(sectionId: String, response: GetVideoResponse) {
        if (response.status == SUCCESS) {
            _lectures.value = BaseResponse.Success(response)
            sectionLectures[sectionId] = response.data as ArrayList<Lecture>
            if (sectionLectures.size == sectionIds.size)
                _isAllLecturesLoaded.value = true
        } else
            _lectures.value = BaseResponse.Error("Error loading lectures")
    }
}
