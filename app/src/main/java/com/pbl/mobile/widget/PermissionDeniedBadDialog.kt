package com.pbl.mobile.widget

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.pbl.mobile.databinding.DialogPermissionCameraDeniedBadBinding

class PermissionDeniedBadDialog(
    private val onYesClickCallback: () -> Unit
) : DialogFragment() {
    private val binding by lazy { DialogPermissionCameraDeniedBadBinding.inflate(layoutInflater) }

    companion object {
        const val TAG = "PermissionDeniedBadDialog_Tag"
        fun newInstance(onYesClickCallback: () -> Unit) =
            PermissionDeniedBadDialog(onYesClickCallback)
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
            btnCancel.setOnClickListener { this@PermissionDeniedBadDialog.dismiss() }
            btnOk.setOnClickListener {
                onYesClickCallback.invoke()
                this@PermissionDeniedBadDialog.dismiss()
            }
        }
    }
}