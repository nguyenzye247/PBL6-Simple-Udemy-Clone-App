package com.pbl.mobile.ui.upload.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.activityViewModels
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.databinding.FragmentUploadSectionBinding
import com.pbl.mobile.ui.main.HomeMainViewModel

class UploadSectionFragment : BaseFragment<FragmentUploadSectionBinding, HomeMainViewModel>() {

    companion object{
        private const val TAG = "FRAGMENT-12"
    }

    private lateinit var onPublishListener: OnPublishListener

    override fun getLazyBinding() = lazy { FragmentUploadSectionBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeMainViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnPublishListener)
            onPublishListener = context
    }

    override fun setupInit() {
        initViews()
        initListener()
        observe()
    }

    private fun initViews() {

    }

    private fun initListener() {
        binding.apply {
            btnPublish.setOnClickListener {
                //TODO check inputs
                onPublishListener.onPublishListener()
            }
        }
    }

    private fun observe() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: FragmentUploadSection")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: FragmentUploadSection")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: FragmentUploadSection")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: FragmentUploadSection")
    }

    interface OnPublishListener {
        fun onPublishListener()
    }
}
