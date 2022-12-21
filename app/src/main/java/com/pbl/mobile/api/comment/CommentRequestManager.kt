package com.pbl.mobile.api.comment

import android.app.Application
import com.pbl.mobile.model.remote.comment.get.GetCommentResponse
import com.pbl.mobile.model.remote.comment.push.PushCommentRequest
import com.pbl.mobile.model.remote.comment.push.PushCommentResponse
import io.reactivex.rxjava3.core.Single

class CommentRequestManager {
    fun push(application: Application, request: PushCommentRequest): Single<PushCommentResponse> {
        return CommentApi.getApi(application).push(request)
    }

    fun getVideoComments(
        application: Application,
        videoId: String,
        page: Int,
        paging: Int
    ): Single<GetCommentResponse> {
        return CommentApi.getApi(application).getVideoComments(videoId, page, paging)
    }
}
