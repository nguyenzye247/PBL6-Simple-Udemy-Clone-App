package com.pbl.mobile.ui.upload.section

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.pbl.mobile.databinding.DialogNewSectionBinding

class NewSectionDialog : DialogFragment() {
    private val binding by lazy { DialogNewSectionBinding.inflate(layoutInflater) }
    private lateinit var onAddSectionListener: OnAddSectionListener

    companion object {
        const val TAG = "NEW_SECTION_DIALOG"
        fun newInstance() = NewSectionDialog()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnAddSectionListener)
            onAddSectionListener = context
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
