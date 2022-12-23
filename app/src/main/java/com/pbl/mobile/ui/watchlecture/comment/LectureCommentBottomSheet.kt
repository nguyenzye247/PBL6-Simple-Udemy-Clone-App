package com.pbl.mobile.ui.watchlecture.comment

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pbl.mobile.R
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.api.SUCCESS
import com.pbl.mobile.common.EMPTY_TEXT
import com.pbl.mobile.databinding.FragmentLectureCommentBottomsheetBinding
import com.pbl.mobile.extension.getBaseConfig
import com.pbl.mobile.extension.showToast
import com.pbl.mobile.model.local.Comment
import com.pbl.mobile.ui.watchlecture.WatchLectureViewModel
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch

class LectureCommentBottomSheet(
    private val lectureId: String,
    private val maxHeight: Int,
    private val onDismissCallback: () -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var commentAdapter: LectureCommentAdapter
    private val comments: ArrayList<Comment> = arrayListOf()
    private val loadedInstructorIds = arrayListOf<String>()

    private val viewModel by lazy { activityViewModels<WatchLectureViewModel>() }.value
    private val binding by lazy {
        FragmentLectureCommentBottomsheetBinding.inflate(
            layoutInflater
        )
    }

    companion object {
        const val TAG = "LectureCommentBottomSheet_Tag"
        fun newInstance(lectureId: String, maxHeight: Int, onDismissCallback: () -> Unit) =
            LectureCommentBottomSheet(lectureId, maxHeight, onDismissCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        activity?.let {
            binding.csLectureBottomSheet.minHeight = maxHeight
        }
        initViews()
        initListeners()
        observe()
    }

    private fun initViews() {
        dialog?.setOnShowListener {
            val bottomSheet =
                dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            (binding.root.parent as View).setBackgroundColor(Color.TRANSPARENT)
        }
        binding.apply {
            rvComments.apply {
                commentAdapter = LectureCommentAdapter()
                adapter = commentAdapter
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }
            Glide.with(this@LectureCommentBottomSheet)
                .load(context?.getBaseConfig()?.myAvatar ?: EMPTY_TEXT)
                .placeholder(R.drawable.avatar_holder_person)
                .into(ivUserCommentAvatar)
        }
    }

    private fun initListeners() {
        binding.apply {
            btnClose.setOnClickListener {
                dismiss()
            }
            commentAdapter.addLoadStateListener { loadStates ->
                if (loadStates.refresh !is LoadState.Loading)
                    progressBarComment.isVisible = false
            }
            btnAddComment.setOnClickListener {
                val commentText = binding.etAddComment.text.toString()
                if (commentText.isNotEmpty()) {
                    viewModel.pushComment(
                        commentText,
                        context?.getBaseConfig()?.myId ?: EMPTY_TEXT,
                        lectureId
                    )
                }
            }
        }
    }

    private fun observe() {
        viewModel.apply {
            getCommentWithPaging(lectureId).observe(this@LectureCommentBottomSheet) {
                commentAdapter.submitData(lifecycle, it)
            }
            getPushComment().observe(this@LectureCommentBottomSheet) { response ->
                when (response) {
                    is BaseResponse.Success -> {
                        response.data?.let { pushCommentResponse ->
                            if (pushCommentResponse.status == SUCCESS) {
                                binding.etAddComment.text?.clear()
                                commentAdapter.refresh()
                            } else {
                                context?.showToast("Error adding your comment")
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
        viewLifecycleOwner.lifecycleScope.launch {
            commentAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .collect {
                    val loadedComments = commentAdapter.snapshot().items as ArrayList<Comment>
                    if (loadedComments.isNotEmpty()) {
                        val originSize = comments.size
                        comments.addAll(loadedComments.subList(originSize, loadedComments.size))

                        val courseUserIds = loadedComments.map { it.userId }
                        val unloadedUserIds = arrayListOf<String>()
                        courseUserIds.forEach { id ->
                            if (!loadedInstructorIds.contains(id)) {
                                unloadedUserIds.add(id)
                                loadedInstructorIds.add(id)
                            }
                        }
                        if (unloadedUserIds.isNotEmpty()) {
                            viewModel.loadUsers(unloadedUserIds)
                        }
                    }
                }
        }
        viewModel.isFinishedLoadCommentUsers()
            .observe(this@LectureCommentBottomSheet) { isFinished ->
                if (isFinished) {
                    commentAdapter.setUser(viewModel.commentUsers)
                }
            }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissCallback.invoke()
    }
}
