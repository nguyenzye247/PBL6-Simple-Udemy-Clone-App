package com.pbl.mobile.ui.upload.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.activityViewModels
import com.pbl.mobile.R
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.databinding.FragmentUploadInformationBinding
import com.pbl.mobile.ui.main.HomeMainViewModel
import com.pbl.mobile.util.ValidationUtils

class UploadInformationFragment :
    BaseFragment<FragmentUploadInformationBinding, HomeMainViewModel>() {

    companion object {
        private const val TAG = "FRAGMENT-11"
    }

    private lateinit var onSubmitListener: OnSubmitListener

    override fun getLazyBinding() =
        lazy { FragmentUploadInformationBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeMainViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSubmitListener)
            onSubmitListener = context
    }

    override fun setupInit() {
        initViews()
        initListener()
        observe()
    }

    private fun initViews() {
        binding.apply {
            btnSubmit.setOnClickListener {
                if (isAllInputValid())
                    onSubmitListener.onSubmitListener()
            }
            llUploadThumbnail.setOnClickListener {
                //TODO: load image
            }
        }
    }

    private fun initListener() {

    }

    private fun observe() {

    }

    private fun isAllInputValid(): Boolean {
        binding.apply {
            val validTitle = ValidationUtils.isUploadInputValid(
                etCourseTitle,
                txtCourseTitle,
                context?.getString(R.string.required_title)
            )
            val validDescription = ValidationUtils.isUploadInputValid(
                etCourseDecription, txtCourseDescription, getString(
                    R.string.require_description
                )
            )
            val validThumbnail = isValidThumbnail()
            val validPrice = ValidationUtils.isUploadInputValid(
                etCoursePrice,
                txtPrice,
                getString(R.string.require_price)
            )
            val validCourse = ValidationUtils.isUploadInputValid(
                etCourseCategory,
                txtHashtag,
                getString(R.string.require_category)
            )
            return validTitle && validDescription && validThumbnail && validPrice && validCourse
        }
    }

    private fun isValidThumbnail(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: FragmentUploadInformation")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: FragmentUploadInformation")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: FragmentUploadInformation")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: FragmentUploadInformation")
    }

    interface OnSubmitListener {
        fun onSubmitListener()
    }
}
