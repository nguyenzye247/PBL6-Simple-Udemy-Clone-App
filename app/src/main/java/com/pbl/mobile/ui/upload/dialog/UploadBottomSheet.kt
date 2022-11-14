package com.pbl.mobile.ui.upload.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pbl.mobile.databinding.FragmentUploadBottomsheetBinding
import com.pbl.mobile.ui.main.OnUploadOptionSelect

class UploadBottomSheet: BottomSheetDialogFragment() {
    private val binding by lazy { FragmentUploadBottomsheetBinding.inflate(layoutInflater) }
    private lateinit var onUploadOptionSelect: OnUploadOptionSelect

    companion object {
        const val TAG: String = "UPLOAD_BOTTOM_SHEET"
        fun newInstance() = UploadBottomSheet()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnUploadOptionSelect)
            onUploadOptionSelect = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListener()
    }

    private fun initViews() {
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
            )
        }
    }

    private fun initListener() {
        binding.apply {
            btnClose.setOnClickListener {
                dismiss()
            }
            tvUploadCourse.setOnClickListener {
                onUploadOptionSelect.onUploadCourseSelect()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

    }
}
