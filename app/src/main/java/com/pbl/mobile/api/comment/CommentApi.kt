package com.pbl.mobile.api.comment

import android.app.Application
import com.pbl.mobile.api.BaseRequestManager
import com.pbl.mobile.api.GET_COMMENTS_URL
import com.pbl.mobile.api.PUSH_COMMENT_URL
import com.pbl.mobile.model.remote.comment.push.PushCommentRequest
import com.pbl.mobile.model.remote.comment.push.PushCommentResponse
import com.pbl.mobile.model.remote.comment.get.GetCommentResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CommentApi {
    companion object {
        fun getApi(application: Application): CommentApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(CommentApi::class.java)
        }
    }

    @POST(PUSH_COMMENT_URL)
    fun push(@Body request: PushCommentRequest): Single<PushCommentResponse>

    @GET("$GET_COMMENTS_URL/{ID}")
    fun getVideoComments(@Query("ID") videoId: String): Single<GetCommentResponse>
}
