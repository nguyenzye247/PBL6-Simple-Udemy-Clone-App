package com.pbl.mobile.widget

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.pbl.mobile.databinding.DialogPermissionCameraDeniedGoodBinding

class PermissionDeniedGoodDialog(
    private val onYesClickCallback: () -> Unit
) : DialogFragment() {
    private val binding by lazy { DialogPermissionCameraDeniedGoodBinding.inflate(layoutInflater) }

    companion object {
        const val TAG = "PermissionDeniedGoodDialog_TAG"
        fun newInstance(onYesClickCallback: () -> Unit) = PermissionDeniedGoodDialog(onYesClickCallback)
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
            btnCancel.setOnClickListener { this@PermissionDeniedGoodDialog.dismiss() }
            btnOk.setOnClickListener {
                onYesClickCallback.invoke()
                this@PermissionDeniedGoodDialog.dismiss()
            }
        }
    }
}