package com.pbl.mobile.api.courses.paging

import android.app.Application
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.pbl.mobile.api.courses.CourseRequestManager
import com.pbl.mobile.common.DEFAULT_PAGE_INDEX
import com.pbl.mobile.extension.observeOnIOThread
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.model.remote.courses.GetCourseResponse
import io.reactivex.rxjava3.core.Single

class CoursePagingSource(
    private val application: Application,
    private val courseRequestManager: CourseRequestManager
) : RxPagingSource<Int, Course>() {
    // The refresh key is used for the initial load of the next PagingSource, after invalidation
    override fun getRefreshKey(state: PagingState<Int, Course>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        } ?: kotlin.run { null }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Course>> {
        val page = params.key ?: DEFAULT_PAGE_INDEX
//        return Single.create {
//            try {
//                val response = courseRequestManager.getCourses(application, page, params.loadSize)
//                val responseData = response.data
//                val result = LoadResult.Page(
//                    responseData,
//                    prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
//                    nextKey = if (responseData.isEmpty()) null else page + 1
//                )
//            } catch (exception: IOException) {
//                it.onError(exception)
//            } catch (exception: HttpException) {
//                it.onError(exception)
//            }
//        }
        return courseRequestManager.getCourses(application, page, params.loadSize)
            .observeOnIOThread()
            .map { toLoadResult(it, page) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(courseResponse: GetCourseResponse, position: Int): LoadResult<Int, Course> {
        return LoadResult.Page(
            data = courseResponse.data,
            prevKey = if (position == DEFAULT_PAGE_INDEX) null else position - 1,
            nextKey = if (courseResponse.data.isEmpty()) null else position + 1
        )
    }
}
