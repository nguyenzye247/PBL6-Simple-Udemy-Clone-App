package com.pbl.mobile.ui.watchlecture.description

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pbl.mobile.databinding.FragmentLectureDescriptionBottomsheetBinding
import com.pbl.mobile.model.local.Lecture
import com.pbl.mobile.util.DateFormatUtils
import com.pbl.mobile.util.HtmlUtils

class LectureDescriptionBottomSheet(
    private val lecture: Lecture?,
    private val likeCount: Int,
    private val maxHeight: Int
) : BottomSheetDialogFragment() {

    private val binding by lazy {
        FragmentLectureDescriptionBottomsheetBinding.inflate(
            layoutInflater
        )
    }

    companion object {
        const val TAG = "LectureDescriptionBottomSheet_Tag"
        fun newInstance(lecture: Lecture?, likeCount: Int, maxHeight: Int) =
            LectureDescriptionBottomSheet(lecture, likeCount, maxHeight)
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
    }

    private fun initViews() {
        dialog?.setOnShowListener {
            val bottomSheet =
                dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            (binding.root.parent as View).setBackgroundColor(Color.TRANSPARENT)
        }
        lecture?.let {
            binding.apply {
                tvLectureTitle.text = lecture.title
                wvLectureDescription.loadData(
                    HtmlUtils.removeHtmlHyphen(lecture.description),
                    "text/html; charset=utf-8",
                    "UTF-8"
                )
                tvLikeCount.text = likeCount.toString()
                tvPublishDay.text = DateFormatUtils.getDayAndMonthFrom(lecture.createdAt)
                tvPublishYear.text = DateFormatUtils.getYearFrom(lecture.createdAt)
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }
}