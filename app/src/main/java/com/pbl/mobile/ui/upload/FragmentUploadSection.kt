package com.pbl.mobile.ui.upload

import androidx.fragment.app.activityViewModels
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.databinding.FragmentUploadSectionBinding
import com.pbl.mobile.ui.main.HomeMainViewModel

class FragmentUploadSection : BaseFragment<FragmentUploadSectionBinding, HomeMainViewModel>() {

    override fun getLazyBinding() = lazy { FragmentUploadSectionBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeMainViewModel>()

    override fun setupInit() {
    }

}
