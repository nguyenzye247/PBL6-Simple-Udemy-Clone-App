package com.pbl.mobile.ui.home.fragment.upload

import androidx.fragment.app.activityViewModels
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.databinding.FragmentUploadSectionBinding
import com.pbl.mobile.ui.home.HomeViewModel

class FragmentUploadSection : BaseFragment<FragmentUploadSectionBinding, HomeViewModel>() {

    override fun getLazyBinding() = lazy { FragmentUploadSectionBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeViewModel>()

    override fun setupInit() {
    }

}
