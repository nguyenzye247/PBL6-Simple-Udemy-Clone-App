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
import com.pbl.mobile.databinding.DialogInstructorRequestConfirmBinding

class InstructorRequestConfirmDialog : DialogFragment() {
    private val binding by lazy { DialogInstructorRequestConfirmBinding.inflate(layoutInflater) }
    private lateinit var onYesClickListener: OnInstructorRequestConfirmListener

    companion object {
        const val TAG = "INSTRUCTOR_REQUEST_CONFIRM_DIALOG"
        fun newInstance() = InstructorRequestConfirmDialog()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnInstructorRequestConfirmListener)
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
            btnCancel.setOnClickListener { this@InstructorRequestConfirmDialog.dismiss() }
            btnOk.setOnClickListener {
                onYesClickListener.onInstructorRequestConfirm()
                this@InstructorRequestConfirmDialog.dismiss()
            }
        }
    }

    interface OnInstructorRequestConfirmListener {
        fun onInstructorRequestConfirm()
    }
}
