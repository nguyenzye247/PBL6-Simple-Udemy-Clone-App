package com.pbl.mobile.ui.home.fragment.upload

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.databinding.FragmentUploadBinding
import com.pbl.mobile.extension.showToast
import com.pbl.mobile.ui.home.HomeViewModel
import com.pbl.mobile.ui.home.fragment.upload.adapter.UploadAdapter

class FragmentUpload : BaseFragment<FragmentUploadBinding, HomeViewModel>() {
    private var uploadAdapter: UploadAdapter? = null

    override fun getLazyBinding(): Lazy<FragmentUploadBinding> =
        lazy { FragmentUploadBinding.inflate(layoutInflater) }

    override fun getLazyViewModel(): Lazy<HomeViewModel> = activityViewModels()

    override fun setupInit() {
        initViews()
        initListener()
    }

    private fun initViews() {
        binding.apply {
            uploadAdapter = UploadAdapter(parentFragmentManager, lifecycle)
            vp2Upload.apply {
                isUserInputEnabled = false
                adapter = uploadAdapter
                registerOnPageChangeCallback(object: OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        Log.d("PAGER-11", "onPageSelected: $position")
                    }
                })
            }
        }
    }

    private fun initListener() {

    }
}