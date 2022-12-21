package com.pbl.mobile.api.comment.paging

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.pbl.mobile.model.local.Comment

interface CommentRxRepository {
    fun getComments(lectureId: String): LiveData<PagingData<Comment>>
}
