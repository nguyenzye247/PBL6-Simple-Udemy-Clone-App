package com.pbl.mobile.ui.upload

import androidx.fragment.app.activityViewModels
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.databinding.FragmentUploadInformationBinding
import com.pbl.mobile.ui.main.HomeMainViewModel

class FragmentUploadInformation: BaseFragment<FragmentUploadInformationBinding, HomeMainViewModel>() {

    override fun getLazyBinding() = lazy { FragmentUploadInformationBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeMainViewModel>()

    override fun setupInit() {
    }
}
