package com.pbl.mobile.ui.home.fragment.upload

import androidx.fragment.app.activityViewModels
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.databinding.FragmentUploadInformationBinding
import com.pbl.mobile.ui.home.HomeViewModel

class FragmentUploadInformation: BaseFragment<FragmentUploadInformationBinding, HomeViewModel>() {

    override fun getLazyBinding() = lazy { FragmentUploadInformationBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeViewModel>()

    override fun setupInit() {
    }
}
