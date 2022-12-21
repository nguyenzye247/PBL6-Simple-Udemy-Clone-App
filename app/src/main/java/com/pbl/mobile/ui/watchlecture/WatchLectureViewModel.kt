package com.pbl.mobile.ui.watchlecture

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.api.comment.CommentRequestManager
import com.pbl.mobile.api.comment.paging.CommentRxRepositoryImpl
import com.pbl.mobile.api.like.VideoLikeRequestManager
import com.pbl.mobile.api.user.UserRequestManager
import com.pbl.mobile.api.video.VideoRequestManager
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.BaseViewModel
import com.pbl.mobile.extension.observeOnUiThread
import com.pbl.mobile.model.local.Comment
import com.pbl.mobile.model.remote.comment.get.GetCommentResponse
import com.pbl.mobile.model.remote.comment.push.PushCommentRequest
import com.pbl.mobile.model.remote.comment.push.PushCommentResponse
import com.pbl.mobile.model.remote.like.GetVideoLikeResponse
import com.pbl.mobile.model.remote.like.PostVideoLikeResponse
import com.pbl.mobile.model.remote.like.request.PostLikeRequestBody
import com.pbl.mobile.model.remote.user.GetSimpleUserResponse
import com.pbl.mobile.model.remote.video.view.GetVideoViewResponse

class WatchLectureViewModel(val input: BaseInput.WatchLectureInput) : BaseViewModel(input) {
    private val userRequestManager = UserRequestManager()
    private val commentRequestManager = CommentRequestManager()
    private val videoRequestManager = VideoRequestManager()
    private val videoLikeRequestManager = VideoLikeRequestManager()
    private val commentRxRepositoryImpl = CommentRxRepositoryImpl(
        input.application,
        commentRequestManager
    )
    private val _instructor: MutableLiveData<BaseResponse<GetSimpleUserResponse>> =
        MutableLiveData()
    private val _previewUser: MutableLiveData<BaseResponse<GetSimpleUserResponse>> =
        MutableLiveData()
    private val _isFinishedLoadCommentUsers: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _comments: MutableLiveData<BaseResponse<GetCommentResponse>> = MutableLiveData()
    private val _views: MutableLiveData<BaseResponse<GetVideoViewResponse>> = MutableLiveData()
    private val _videoLikes: MutableLiveData<BaseResponse<GetVideoLikeResponse>> = MutableLiveData()
    private val _isInFullscreenMode: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _isLikeVideo: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _likeVideoAction: MutableLiveData<BaseResponse<PostVideoLikeResponse>> =
        MutableLiveData()
    private val _pushComment: MutableLiveData<BaseResponse<PushCommentResponse>> = MutableLiveData()

    fun instructor(): LiveData<BaseResponse<GetSimpleUserResponse>> = _instructor
    fun previewUser(): LiveData<BaseResponse<GetSimpleUserResponse>> = _previewUser
    fun comments(): LiveData<BaseResponse<GetCommentResponse>> = _comments
    fun views(): LiveData<BaseResponse<GetVideoViewResponse>> = _views
    fun videoLikes(): LiveData<BaseResponse<GetVideoLikeResponse>> = _videoLikes
    fun isUserLikedVideo(): LiveData<Boolean> = _isLikeVideo
    fun videoLikeAction(): LiveData<BaseResponse<PostVideoLikeResponse>> = _likeVideoAction
    fun isInFullScreenMode(): LiveData<Boolean> = _isInFullscreenMode
    fun isFinishedLoadCommentUsers(): LiveData<Boolean> = _isFinishedLoadCommentUsers
    fun getPushComment(): LiveData<BaseResponse<PushCommentResponse>> = _pushComment

    val commentUsers: ArrayList<GetSimpleUserResponse.User> = arrayListOf()

    fun getInstructor(userId: String) {
        subscription.add(
            userRequestManager.getSimpleUserById(input.application, userId)
                .observeOnUiThread()
                .subscribe(
                    { simpleUserResponse ->
                        _instructor.value = BaseResponse.Success(simpleUserResponse)
                    },
                    { throwable ->
                        _instructor.value = BaseResponse.Error(throwable.message)
                    }
                )
        )
    }

    fun getPreviewUser(userId: String) {
        subscription.add(
            userRequestManager.getSimpleUserById(input.application, userId)
                .observeOnUiThread()
                .doOnSubscribe {
                    _previewUser.value = BaseResponse.Loading()
                }
                .subscribe(
                    { simpleUserResponse ->
                        _previewUser.value = BaseResponse.Success(simpleUserResponse)
                    },
                    { throwable ->
                        _previewUser.value = BaseResponse.Error(throwable.message)
                    }
                )
        )
    }

    fun loadUsers(userIds: List<String>) {
        userIds.forEach { userId ->
            subscription.add(
                userRequestManager.getSimpleUserById(input.application, userId)
                    .observeOnUiThread()
                    .subscribe(
                        { simpleUserResponse ->
                            if (!commentUsers.contains(simpleUserResponse.data)) {
                                commentUsers.add(simpleUserResponse.data)
                                Log.d("COMMENT_USER-123", "$commentUsers")
                                _isFinishedLoadCommentUsers.value =
                                    commentUsers.size == userIds.size
                            }
                        },
                        { throwable ->

                        }
                    )
            )
        }
    }

    fun getComments2(lectureId: String): LiveData<PagingData<Comment>> =
        commentRxRepositoryImpl.getComments(lectureId)

    fun getComments(lectureId: String) {
        subscription.add(
            commentRequestManager.getVideoComments(
                input.application,
                lectureId,
                1,
                10
            ).observeOnUiThread()
                .doOnSubscribe {
                    _comments.value = BaseResponse.Loading()
                }
                .subscribe(
                    { commentsResponse ->
                        _comments.value = BaseResponse.Success(commentsResponse)
                    },
                    { throwable ->
                        _comments.value = BaseResponse.Error(throwable.message)
                    }
                )
        )
    }

    fun pushComment(content: String, userId: String, videoId: String) {
        subscription.add(
            commentRequestManager.push(
                input.application,
                PushCommentRequest(userId, content, videoId)
            )
                .observeOnUiThread()
                .subscribe(
                    { pushCommentResponse ->
                        _pushComment.value = BaseResponse.Success(pushCommentResponse)
                    },
                    { throwable ->
                        _pushComment.value = BaseResponse.Error(throwable.message)
                    }
                )
        )
    }

    fun getVideoViews(userId: String, lectureId: String) {
        subscription.add(
            videoRequestManager.getVideoViews(
                input.application,
                userId,
                lectureId
            )
                .observeOnUiThread()
                .doOnSubscribe {
                    _views.value = BaseResponse.Loading()
                }
                .subscribe(
                    { viewResponse ->
                        _views.value = BaseResponse.Success(viewResponse)
                    },
                    { throwable ->
                        _views.value = BaseResponse.Error(throwable.message)
                    }
                )
        )
    }

    fun getVideoLikes(videoId: String) {
        subscription.add(
            videoLikeRequestManager.getVideoLike(
                input.application,
                videoId
            )
                .observeOnUiThread()
                .subscribe(
                    { videoLikeResponse ->
                        _videoLikes.value = BaseResponse.Success(videoLikeResponse)
                    },
                    { throwable ->
                        _videoLikes.value = BaseResponse.Error(throwable.message)
                    }
                )
        )
    }

    fun likeVideo(userId: String, videoId: String) {
        subscription.add(
            videoLikeRequestManager.likeVideo(
                input.application,
                PostLikeRequestBody(userId, videoId)
            )
                .observeOnUiThread()
                .subscribe(
                    { likeResponse ->
                        _likeVideoAction.value = BaseResponse.Success(likeResponse)
                    },
                    { throwable ->
                        _likeVideoAction.value = BaseResponse.Error(throwable.message)
                    }
                )
        )
    }

    fun checkUserLikeVideo(userId: String, videoId: String) {
        subscription.add(
            videoLikeRequestManager.checkLikeVideo(
                input.application,
                videoId,
                userId
            )
                .observeOnUiThread()
                .subscribe(
                    { checkLikeResponse ->
                        checkLikeResponse.data?.let {
                            _isLikeVideo.value = it.isLike
                        }
                    },
                    {
                        _isLikeVideo.value = false
                    }
                )
        )
    }

    fun changeFullscreenModeValue() {
        _isInFullscreenMode.value?.let {
            _isInFullscreenMode.value = !it
        }
    }
}
