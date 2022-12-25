package com.pbl.mobile.api.courses.paging.source

import android.app.Application
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.pbl.mobile.api.courses.CourseRequestManager
import com.pbl.mobile.common.DEFAULT_PAGE_INDEX
import com.pbl.mobile.extension.observeOnIOThread
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.model.remote.courses.GetInstructorCourseResponse
import io.reactivex.rxjava3.core.Single

class InstructorCoursePagingSource(
    private val application: Application,
    private val userId: String,
    private val courseRequestManager: CourseRequestManager
) : RxPagingSource<Int, Course>() {
    override fun getRefreshKey(state: PagingState<Int, Course>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        } ?: kotlin.run { null }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Course>> {
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return courseRequestManager.getInstructorCourses(application, userId)
            .observeOnIOThread()
            .map { toLoadResult(it, page) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(
        courseResponse: GetInstructorCourseResponse,
        position: Int
    ): LoadResult<Int, Course> {
        return LoadResult.Page(
            data = courseResponse.data,
            prevKey = if (position == DEFAULT_PAGE_INDEX) null else position - 1,
            nextKey = if (courseResponse.pagination.next_page == null) null else position + 1
        )
    }
}
