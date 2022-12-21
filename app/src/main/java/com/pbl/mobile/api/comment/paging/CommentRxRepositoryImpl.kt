package com.pbl.mobile.api.comment.paging

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.pbl.mobile.api.comment.CommentRequestManager
import com.pbl.mobile.model.local.Comment

class CommentRxRepositoryImpl(
    private val application: Application,
    private val commentRequestManager: CommentRequestManager
) : CommentRxRepository {
    override fun getComments(lectureId: String): LiveData<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 30,
                prefetchDistance = 6,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                CommentPagingSource(
                    application,
                    lectureId,
                    commentRequestManager
                )
            }
        ).liveData
    }
}
