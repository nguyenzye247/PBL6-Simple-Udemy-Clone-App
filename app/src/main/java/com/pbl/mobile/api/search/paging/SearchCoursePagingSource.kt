package com.pbl.mobile.api.search.paging

import android.app.Application
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.pbl.mobile.api.search.SearchRequestManager
import com.pbl.mobile.common.DEFAULT_PAGE_INDEX
import com.pbl.mobile.extension.observeOnIOThread
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.model.remote.search.SearchResponse
import io.reactivex.rxjava3.core.Single

class SearchCoursePagingSource(
    private val application: Application,
    private val searchRequestManager: SearchRequestManager,
    private val key: String,
    private val minPrice: Long,
    private val maxPrice: Long,
    private val category: String,
    private val tag: String
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
        return searchRequestManager.searchCourses(
            application,
            page,
            params.loadSize,
            key,
            minPrice, maxPrice,
            category, tag
        )
            .observeOnIOThread()
            .map { toLoadResult(it, page) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(
        courseResponse: SearchResponse,
        position: Int
    ): LoadResult<Int, Course> {
        return LoadResult.Page(
            data = courseResponse.data,
            prevKey = if (position == DEFAULT_PAGE_INDEX) null else position - 1,
            nextKey = if (courseResponse.pagination.next_page == null) null else position + 1
        )
    }
}
