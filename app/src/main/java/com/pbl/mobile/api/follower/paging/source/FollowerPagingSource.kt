package com.pbl.mobile.api.follower.paging.source

import android.app.Application
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.pbl.mobile.api.follower.FollowerRequestManager
import com.pbl.mobile.common.DEFAULT_PAGE_INDEX
import com.pbl.mobile.extension.observeOnIOThread
import com.pbl.mobile.model.remote.follower.GetFollowerOfInstructorResponse
import io.reactivex.rxjava3.core.Single

class FollowerPagingSource(
    private val application: Application,
    private val followerRequestManager: FollowerRequestManager
) : RxPagingSource<Int, GetFollowerOfInstructorResponse.Data>() {
    // The refresh key is used for the initial load of the next PagingSource, after invalidation
    override fun getRefreshKey(state: PagingState<Int, GetFollowerOfInstructorResponse.Data>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        } ?: kotlin.run { null }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, GetFollowerOfInstructorResponse.Data>> {
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return followerRequestManager.getFollowersOfInstructor(application,"", page, params.loadSize)
            .observeOnIOThread()
            .map { toLoadResult(it, page) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(
        courseResponse: GetFollowerOfInstructorResponse,
        position: Int
    ): LoadResult<Int, GetFollowerOfInstructorResponse.Data> {
        return LoadResult.Page(
            data = courseResponse.data,
            prevKey = if (position == DEFAULT_PAGE_INDEX) null else position - 1,
            nextKey = if (courseResponse.pagination.next_page == null) null else position + 1
        )
    }
}