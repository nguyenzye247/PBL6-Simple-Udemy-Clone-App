package com.pbl.mobile.api.comment

import android.app.Application
import com.pbl.mobile.model.remote.comment.PushCommentRequest
import com.pbl.mobile.model.remote.comment.PushCommentResponse
import com.pbl.mobile.model.remote.comment.get.GetCommentResponse
import io.reactivex.rxjava3.core.Single

class CommentRequestManager {
    fun push(application: Application, request: PushCommentRequest): Single<PushCommentResponse> {
        return CommentApi.getApi(application).push(request)
    }

    fun getVideoComments(application: Application, videoId: String): Single<GetCommentResponse> {
        return CommentApi.getApi(application).getVideoComments(videoId)
    }
}
