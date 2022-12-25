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
import com.pbl.mobile.databinding.DialogSignOutConfirmBinding

class SignOutConfirmDialog : DialogFragment() {
    private val binding by lazy { DialogSignOutConfirmBinding.inflate(layoutInflater) }
    private lateinit var onYesClickListener: OnSignOutConfirmListener

    companion object {
        const val TAG = "SIGN_OUT_CONFIRM_DIALOG"
        fun newInstance() = SignOutConfirmDialog()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSignOutConfirmListener)
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
            btnCancel.setOnClickListener { this@SignOutConfirmDialog.dismiss() }
            btnOk.setOnClickListener {
                onYesClickListener.onSignOutConfirm()
                this@SignOutConfirmDialog.dismiss()
            }
        }
    }

    interface OnSignOutConfirmListener {
        fun onSignOutConfirm()
    }
}
