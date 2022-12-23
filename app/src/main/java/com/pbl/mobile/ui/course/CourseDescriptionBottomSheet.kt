package com.pbl.mobile.ui.course

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pbl.mobile.databinding.FragmentCourseDescriptionBottomsheetBinding
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.util.AppWebViewClients
import com.pbl.mobile.util.HtmlUtils
import com.pbl.mobile.util.ScreenUtils

class CourseDescriptionBottomSheet(
    private val course: Course
) : BottomSheetDialogFragment() {

    private val binding by lazy { FragmentCourseDescriptionBottomsheetBinding.inflate(layoutInflater) }

    companion object {
        const val TAG = "CourseDescriptionBottomSheet_Tag"

        const val HEIGHT_FACTOR = 0.8
        fun newInstance(course: Course) =
            CourseDescriptionBottomSheet(course)
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
            val maxHeight = HEIGHT_FACTOR * ScreenUtils.getScreenHeight(it)
            binding.csBottomSheet.minHeight = maxHeight.toInt()
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
        binding.apply {
            progressBar.isVisible = true
            tvCourseTitle.text = course.name
//            tvCreatedByPerson.text = ""
            wvCourseDescription.webViewClient = AppWebViewClients(
                onFinishedLoading = {
                    progressBar.isVisible = false
                }
            )
            wvCourseDescription.loadData(
                HtmlUtils.removeHtmlHyphen(course.description),
                "text/html; charset=utf-8",
                "UTF-8"
            )
        }
    }

    private fun initListeners() {
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }
}
