package com.pbl.mobile.ui.watchlecture

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.pbl.mobile.R
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.ViewModelProviderFactory
import com.pbl.mobile.common.*
import com.pbl.mobile.databinding.ActivityWatchLectureBinding
import com.pbl.mobile.extension.getBaseConfig
import com.pbl.mobile.extension.showToast
import com.pbl.mobile.model.local.Comment
import com.pbl.mobile.model.local.Lecture
import com.pbl.mobile.model.remote.user.GetSimpleUserResponse
import com.pbl.mobile.model.remote.video.view.UpdateViewBody
import com.pbl.mobile.ui.course.lecture.LectureAdapter
import com.pbl.mobile.ui.watchlecture.comment.LectureCommentBottomSheet
import com.pbl.mobile.ui.watchlecture.description.LectureDescriptionBottomSheet
import com.pbl.mobile.util.DateFormatUtils
import com.pbl.mobile.util.ScreenUtils
import java.math.RoundingMode

class WatchLectureActivity : BaseActivity<ActivityWatchLectureBinding, WatchLectureViewModel>() {
    private var player: ExoPlayer? = null
    private val comments: ArrayList<Comment> = arrayListOf()
    private var previewUser: GetSimpleUserResponse.User? = null
    private lateinit var lectureAdapter: LectureAdapter

    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L
    private var likeCount: Int = 0

    companion object {
        private const val SKIP_AMOUNT = 5 //second
    }

    override fun getLazyBinding() = lazy { ActivityWatchLectureBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<WatchLectureViewModel> {
        ViewModelProviderFactory(
            BaseInput.WatchLectureInput(
                application,
                getLectureFromIntent()
            )
        )
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
//        hideSystemUI()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    override fun setupInit() {
        initViews()
        initListeners()
        observe()
    }

    private fun initViews() {
        val lecture = getLectureFromIntent()
        val categoryName = intent.getStringExtra(CATEGORY_KEY)
        val isCoursePurchased = intent.getBooleanExtra(IS_PURCHASED_COURSES_KEY, false)
        val lectures = intent.getParcelableArrayListExtra<Lecture>(LIST_LECTURE_KEY)
            ?: arrayListOf()
        binding.apply {
            lecture?.let { lec ->
                tvCategory.text = categoryName
                tvLectureTitle.text = lec.title
                tvVideoPublishTime.text = DateFormatUtils.parseDate(lec.createdAt)
                tvVideoViewCount.text = lec.totalView.toString()
            }
            rvLectures.apply {
                lectureAdapter = LectureAdapter(
                    lecture?.id ?: EMPTY_TEXT,
                    isCoursePurchased,
                    lectures,
                    onLectureItemClickCallback = {
                        if (it.id != lecture?.id) {
                            this@WatchLectureActivity.finish()
                            startActivity(
                                Intent(
                                    this@WatchLectureActivity,
                                    WatchLectureActivity::class.java
                                ).apply {
                                    putExtra(LECTURE_KEY, it)
                                    putExtra(CATEGORY_KEY, categoryName)
                                    putExtra(IS_PURCHASED_COURSES_KEY, isCoursePurchased)
                                    putParcelableArrayListExtra(LIST_LECTURE_KEY, lectures)
                                }
                            )
                        }
                    }
                )
                adapter = lectureAdapter
                layoutManager = LinearLayoutManager(
                    this@WatchLectureActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }
        }
    }

    private fun initListeners() {
        val lecture = getLectureFromIntent()
        binding.apply {
            tvMore.setOnClickListener { showLectureDescriptionDialog() }
            llVideoInfo.setOnClickListener { tvMore.performClick() }
            cvComments.setOnClickListener {
                csComments.performClick()
            }
            csComments.setOnClickListener {
                showCommentsDialog()
            }
            btnLike.setOnClickListener {
                viewModel.likeVideo(
                    this@WatchLectureActivity.getBaseConfig().myId,
                    lecture?.id ?: EMPTY_TEXT
                )
            }
        }
        findViewById<View>(com.google.android.exoplayer2.ui.R.id.exo_rew_with_amount).setOnClickListener {
            player?.let {
                it.seekTo(it.currentPosition - SKIP_AMOUNT * 1000)
            }
        }
        findViewById<View>(com.google.android.exoplayer2.ui.R.id.exo_ffwd_with_amount).setOnClickListener {
            player?.let {
                it.seekTo(it.currentPosition + SKIP_AMOUNT * 1000)
            }
        }
        findViewById<View>(com.google.android.exoplayer2.ui.R.id.exo_fullscreen).setOnClickListener {
            player?.let {
                viewModel.apply {
                    changeFullscreenModeValue()
                }
            }
        }
    }

    private fun observe() {
        val lecture = getLectureFromIntent()
        observeInstructor(lecture?.userId ?: EMPTY_TEXT)
        observeComment(lecture?.id ?: EMPTY_TEXT)
        //TODO: have not implement get video views
        observeVideoView(lecture?.userId ?: EMPTY_TEXT, lecture?.id ?: EMPTY_TEXT)
        observeFullscreenMode()
        observePreviewUser()
        observeVideoLikes(
            this.getBaseConfig().myId,
            lecture?.id ?: EMPTY_TEXT
        )
        observeLikeVideoAction(lecture?.id ?: EMPTY_TEXT)
    }

    private fun observeInstructor(instructorId: String) {
        viewModel.getInstructor(instructorId)
        viewModel.instructor().observe(this@WatchLectureActivity) { response ->
            when (response) {
                is BaseResponse.Success -> {
                    response.data?.let { instructorResponse ->
                        binding.tvInstructorName.text = instructorResponse.data.fullName
                        Glide.with(this@WatchLectureActivity)
                            .load(instructorResponse.data.avatarUrl)
                            .placeholder(R.drawable.avatar_holder_person)
                            .into(binding.ivInstructorAvatar)
                    } ?: kotlin.run {
                        binding.tvInstructorName.text = getString(R.string.unknown)
                    }
                }
                is BaseResponse.Error -> {
                    binding.tvInstructorName.text = getString(R.string.unknown)
                }
                else -> {
                    //no-ops
                }
            }
        }
    }

    private fun observeComment(lectureId: String) {
        viewModel.comments().observe(this@WatchLectureActivity) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    binding.progressBarComment.isVisible = true
                }
                is BaseResponse.Success -> {
                    val responseCommentsData = response.data
                    responseCommentsData?.let {
                        comments.clear()
                        comments.addAll(it.comments)
                        comments.firstOrNull()?.let { previewComment ->
                            viewModel.getPreviewUser(previewComment.userId)
                        }
                    }
                    binding.progressBarComment.isVisible = false
                }
                is BaseResponse.Error -> {
                    showToast("Error loading comments")
                    binding.progressBarComment.isVisible = false
                }
            }
        }
        viewModel.getComments(lectureId)
    }

    private fun observePreviewUser() {
        viewModel.previewUser().observe(this@WatchLectureActivity) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    binding.progressBarComment.isVisible = true
                }
                is BaseResponse.Success -> {
                    response.data?.let { simpleUser ->
                        previewUser = simpleUser.data
                    }
                    setPreviewComment(comments.firstOrNull())
                    binding.progressBarComment.isVisible = false
                }
                is BaseResponse.Error -> {
                    // no-ops
                    binding.progressBarComment.isVisible = false
                }
            }
        }
    }

    private fun observeVideoView(instructorId: String, lectureId: String) {
        viewModel.views().observe(this@WatchLectureActivity) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    // no-ops
                }
                is BaseResponse.Success -> {
                    val data = response.data
                    data?.let { viewData ->
                        val lastDuration = (viewData.data.highestDuration * 1000).toLong()
                        player?.seekTo(lastDuration)
                        val viewCountText = "0 Views"
                        binding.tvVideoViewCount.text = viewCountText
                    } ?: kotlin.run {
                        val viewCountText = "Unknown"
                        binding.tvVideoViewCount.text = viewCountText
                    }
                }
                is BaseResponse.Error -> {
                    val viewCountText = "Unknown"
                    binding.tvVideoViewCount.text = viewCountText
                }
            }
        }
        viewModel.getVideoViews(instructorId, lectureId)
    }

    private fun observeFullscreenMode() {
        viewModel.isInFullScreenMode().observe(this@WatchLectureActivity) { isFull ->
            if (isFull) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                binding.playerView.resizeMode = RESIZE_MODE_ZOOM
                ScreenUtils.hideSystemUI(this@WatchLectureActivity, binding.csWatchLayout)
            } else {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                binding.playerView.resizeMode = RESIZE_MODE_FIT
                ScreenUtils.showSystemUI(this@WatchLectureActivity, binding.csWatchLayout)
            }
        }
    }

    private fun observeVideoLikes(myId: String, videoId: String) {
        viewModel.isUserLikedVideo().observe(this@WatchLectureActivity) { isLiked ->
            if (isLiked) {
                //TODO: UI for like video
                binding.btnLike.setIconResource(R.drawable.ic_heart_fill)
            } else {
                //TODO: UI for non like video
                binding.btnLike.setIconResource(R.drawable.ic_like)
            }
        }
        viewModel.checkUserLikeVideo(myId, videoId)
        viewModel.videoLikes().observe(this@WatchLectureActivity) { response ->
            when (response) {
                is BaseResponse.Success -> {
                    response.data?.let { videoLikeResponse ->
                        likeCount = videoLikeResponse.data.size
                        val likeCountText =
                            likeCount.toString() + EMPTY_SPACE + getString(R.string.likes)
                        binding.btnLike.text = likeCountText
                    }
                }
                is BaseResponse.Error -> {
                    val noLikeText = "0" + EMPTY_SPACE + getString(R.string.likes)
                    binding.btnLike.text = noLikeText
                }
                else -> {
                    //no-ops
                }
            }
        }
        viewModel.getVideoLikes(videoId)
    }

    private fun observeLikeVideoAction(videoId: String) {
        viewModel.videoLikeAction().observe(this@WatchLectureActivity) { response ->
            when (response) {
                is BaseResponse.Success -> {
                    response.data?.let { likeActionResponse ->
                        if (likeActionResponse.data?.isLike == true) {
                            //TODO: UI for liked
                            binding.btnLike.setIconResource(R.drawable.ic_heart_fill)
                            viewModel.getVideoLikes(videoId)
                        } else {
                            //TODO: UI for non like
                            binding.btnLike.setIconResource(R.drawable.ic_like)
                            viewModel.getVideoLikes(videoId)
                        }
                    }
                }
                is BaseResponse.Error -> {

                }
                else -> {
                    //no-ops
                }
            }
        }
    }

    private fun initializePlayer() {
        val lecture = getLectureFromIntent()

        val trackSelector = DefaultTrackSelector(this).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }

        player = ExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()
            .also { exoPlayer ->
                val testUrl =
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
                binding.playerView.player = exoPlayer
                val mediaItem = MediaItem.Builder()
                    .setUri(Uri.parse(lecture?.url ?: testUrl))
                    .setMimeType(MimeTypes.APPLICATION_MP4)
                    .build()

                exoPlayer.setMediaItem(mediaItem)

                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentItem, playbackPosition)
                exoPlayer.addListener(playbackStateListener())
                exoPlayer.prepare()
            }
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playWhenReady = exoPlayer.playWhenReady
            currentItem = exoPlayer.currentMediaItemIndex
            playbackPosition = exoPlayer.currentPosition
            exoPlayer.removeListener(playbackStateListener())
            exoPlayer.release()
        }
        player = null
    }

    private fun getLectureFromIntent(): Lecture? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(LECTURE_KEY, Lecture::class.java)
        else
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(LECTURE_KEY)
    }

    private fun showLectureDescriptionDialog() {
        val lecture = getLectureFromIntent()
        val lectureDescriptionBottomSheet = LectureDescriptionBottomSheet.newInstance(
            lecture,
            likeCount,
            getLectureBottomSheetHeight()
        )
        lectureDescriptionBottomSheet.show(
            supportFragmentManager, LectureDescriptionBottomSheet.TAG
        )
    }

    private fun showCommentsDialog() {
        val lectureId = getLectureFromIntent()?.id ?: EMPTY_TEXT
        val lectureCommentsBottomSheet = LectureCommentBottomSheet.newInstance(
            lectureId,
            getLectureBottomSheetHeight(),
            onDismissCallback = {
                viewModel.getComments(lectureId)
            }
        )
        lectureCommentsBottomSheet.show(supportFragmentManager, LectureCommentBottomSheet.TAG)
    }

    private fun getLectureBottomSheetHeight(): Int {
        val screenHeight = ScreenUtils.getScreenHeight(this@WatchLectureActivity)
        val playerViewHeight = binding.playerView.height
        return screenHeight - playerViewHeight - 5
    }

    private fun setPreviewComment(comment: Comment?) {
        comment?.let { comment ->
            previewUser?.let { user ->
                binding.apply {
                    Glide.with(this@WatchLectureActivity)
                        .load(user.avatarUrl)
                        .placeholder(R.drawable.avatar_holder_person)
                        .into(ivPreviewUserCommentAvatar)
                    tvCommentPreview.text = comment.content
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        player?.let { player ->
            getLectureFromIntent()?.let { lecture ->
                val lastDurationInSecond = player.currentPosition / 1000f
                val roundedDuration =
                    lastDurationInSecond.toBigDecimal().setScale(3, RoundingMode.UP).toFloat()
                val userId = this.getBaseConfig().myId
                val lectureId = lecture.id
                val time = DateFormatUtils.getTimeZoneDate()
                viewModel.updateVideoView(
                    UpdateViewBody(
                        roundedDuration,
                        time,
                        userId,
                        lectureId
                    )
                )
            }
        }
    }
}

private const val TAG = "EXOTAG"

private fun playbackStateListener() = object : Player.Listener {
    override fun onPlaybackStateChanged(playbackState: Int) {
        val stateString: String = when (playbackState) {
            ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE"
            ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING"
            ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY"
            ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED"
            else -> "UNKNOWN STATE"
        }
        Log.d(TAG, "Change estate to: $stateString")
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        val playingString: String = if (isPlaying) "PLAYING" else "NOT PLAYING"
        Log.d(TAG, "PLayer is currently: $playingString ")
    }

    override fun onRenderedFirstFrame() {
        // initial frame was rendered
    }

    override fun onEvents(player: Player, events: Player.Events) {
        // Reports all state changes that happen in single iteration together
        // Useful if want to use multiple states value together
        // Example:
        if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED) ||
            events.contains(Player.EVENT_IS_PLAYING_CHANGED)
        ) {
            // Update the UI
            Log.d(TAG, "Player UI need to be updated")
        }
        // When switch media item -> starts at 5 second time stamp
        if (events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)) {
            player.seekTo(5000L)
        }
    }
}
