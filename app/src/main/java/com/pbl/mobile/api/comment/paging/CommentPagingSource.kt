package com.pbl.mobile.api.comment.paging

import android.app.Application
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.pbl.mobile.api.comment.CommentRequestManager
import com.pbl.mobile.common.DEFAULT_PAGE_INDEX
import com.pbl.mobile.extension.observeOnIOThread
import com.pbl.mobile.model.local.Comment
import com.pbl.mobile.model.remote.comment.get.GetCommentResponse
import io.reactivex.rxjava3.core.Single

class CommentPagingSource(
    private val application: Application,
    private val videoId: String,
    private val commentRequestManager: CommentRequestManager
) : RxPagingSource<Int, Comment>() {
    // The refresh key is used for the initial load of the next PagingSource, after invalidation
    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        } ?: kotlin.run { null }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Comment>> {
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return commentRequestManager.getVideoComments(application, videoId, page, params.loadSize)
            .observeOnIOThread()
            .map { toLoadResult(it, page) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(
        commentResponse: GetCommentResponse,
        position: Int
    ): LoadResult<Int, Comment> {
        val hasNextPage = commentResponse.pagination.next_page != null
        return LoadResult.Page(
            commentResponse.comments,
            prevKey = if (position == DEFAULT_PAGE_INDEX) null else position - 1,
            nextKey = if (hasNextPage) null else position + 1
        )
    }
}