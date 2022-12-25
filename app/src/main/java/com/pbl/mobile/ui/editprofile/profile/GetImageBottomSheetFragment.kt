package com.pbl.mobile.ui.editprofile.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pbl.mobile.databinding.FragmentPickImageBottomsheetBinding

class GetImageBottomSheetFragment(
    private val onTakePictureItemClickCallback: () -> Unit,
    private val onOpenGalleryItemClickCallback: () -> Unit
) : BottomSheetDialogFragment() {

    private val binding by lazy {
        FragmentPickImageBottomsheetBinding.inflate(
            layoutInflater
        )
    }

    companion object {
        const val TAG = "GetImageBottomSheetFragment_Tag"
        fun newInstance(
            onTakePictureItemClickCallback: () -> Unit,
            onOpenGalleryItemClickCallback: () -> Unit
        ) = GetImageBottomSheetFragment(
            onTakePictureItemClickCallback,
            onOpenGalleryItemClickCallback
        )
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
        view?.let { view ->
            val params = view.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(10, 0, 10, 16)
            view.layoutParams = params
        }
    }

    private fun initListeners() {
        binding.apply {
            tvTakePicture.setOnClickListener {
                onTakePictureItemClickCallback.invoke()
                dismiss()
            }
            tvGallery.setOnClickListener {
                onOpenGalleryItemClickCallback.invoke()
                dismiss()
            }
        }
    }
}
