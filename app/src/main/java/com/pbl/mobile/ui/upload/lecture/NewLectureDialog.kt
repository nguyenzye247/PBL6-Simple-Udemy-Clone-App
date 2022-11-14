package com.pbl.mobile.ui.upload.lecture

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.pbl.mobile.databinding.DialogNewLectureBinding

class NewLectureDialog : DialogFragment() {
    private val binding by lazy { DialogNewLectureBinding.inflate(layoutInflater) }
    private lateinit var onAddLectureListener: OnAddLectureListener

    companion object {
        const val TAG = "NEW_LECTURE_DIALOG"
        fun newInstance() = NewLectureDialog()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnAddLectureListener)
            onAddLectureListener = context
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
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
            )

        }
        binding.apply {

        }
    }

    private fun initListeners() {
        binding.apply {

        }
    }
}
