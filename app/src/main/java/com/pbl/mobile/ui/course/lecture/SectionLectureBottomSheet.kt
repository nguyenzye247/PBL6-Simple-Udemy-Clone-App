package com.pbl.mobile.ui.course.lecture

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pbl.mobile.databinding.FragmentSectionLecturesBottomsheetBinding
import com.pbl.mobile.model.local.Lecture
import com.pbl.mobile.util.ScreenUtils

class SectionLectureBottomSheet(
    private val sectionName: String,
    private val lectures: ArrayList<Lecture>,
    private val isPurchasedCourse: Boolean
) : BottomSheetDialogFragment() {
    private val binding by lazy { FragmentSectionLecturesBottomsheetBinding.inflate(layoutInflater) }
    private lateinit var lectureAdapter: LectureAdapter
    private lateinit var onLectureItemSelect: OnLectureItemSelectListener

    companion object {
        const val TAG = "SectionLecturesBottomSheet_Tag"
        const val HEIGHT_FACTOR = 0.8
        fun newInstance(
            sectionName: String,
            lectures: ArrayList<Lecture>,
            isPurchasedCourse: Boolean
        ) =
            SectionLectureBottomSheet(sectionName, lectures, isPurchasedCourse)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLectureItemSelectListener)
            onLectureItemSelect = context
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
            binding.csSectionLectures.minHeight = maxHeight.toInt()
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
            tvSectionTitle.text = sectionName
            rvLectures.apply {
                lectureAdapter = LectureAdapter(
                    isPurchasedCourse,
                    lectures,
                    onLectureItemClickCallback = {
                        onLectureItemSelect.onLectureItemSelect(it, lectures)
                    }
                )
                adapter = lectureAdapter
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }
        }
    }

    private fun initListeners() {
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    interface OnLectureItemSelectListener {
        fun onLectureItemSelect(lecture: Lecture, lectures: ArrayList<Lecture>)
    }
}
