package com.pbl.mobile.ui.upload

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.databinding.FragmentUploadBinding
import com.pbl.mobile.ui.main.HomeMainViewModel
import com.pbl.mobile.ui.upload.adapter.UploadAdapter

class FragmentUpload : BaseFragment<FragmentUploadBinding, HomeMainViewModel>() {
    private var uploadAdapter: UploadAdapter? = null

    override fun getLazyBinding(): Lazy<FragmentUploadBinding> =
        lazy { FragmentUploadBinding.inflate(layoutInflater) }

    override fun getLazyViewModel(): Lazy<HomeMainViewModel> = activityViewModels()

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