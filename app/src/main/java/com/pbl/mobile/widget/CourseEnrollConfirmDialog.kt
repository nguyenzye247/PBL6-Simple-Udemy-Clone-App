package com.pbl.mobile.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.pbl.mobile.databinding.DialogEnrollCourseConfirmBinding

class CourseEnrollConfirmDialog : DialogFragment() {
    private val binding by lazy { DialogEnrollCourseConfirmBinding.inflate(layoutInflater) }
    private lateinit var onYesClickListener: OnDialogConfirmListener

    companion object {
        const val TAG = "ENROLL_CONFIRM_DIALOG"
        fun newInstance() = CourseEnrollConfirmDialog()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDialogConfirmListener)
            onYesClickListener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
    }

    private fun initViews() {
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
            )
        }
    }

    private fun initListeners() {
        binding.apply {
            btnCancel.setOnClickListener { this@CourseEnrollConfirmDialog.dismiss() }
            btnOk.setOnClickListener {
                onYesClickListener.onEnrollConfirm()
                this@CourseEnrollConfirmDialog.dismiss()
            }
        }
    }


    interface OnDialogConfirmListener {
        fun onEnrollConfirm()
    }
}
