package com.pbl.mobile.ui.course

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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

        const val HEIGHT_FACTOR = 0.9
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
        bottomSheetBehavior.isDraggable = false
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        activity?.let {
            val maxHeight = HEIGHT_FACTOR * ScreenUtils.getScreenHeight(it)
            binding.csBottomSheet.minHeight = maxHeight.toInt()
        }
        initViews()
        initListeners()
    }

    private fun initViews() {
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
            )
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
