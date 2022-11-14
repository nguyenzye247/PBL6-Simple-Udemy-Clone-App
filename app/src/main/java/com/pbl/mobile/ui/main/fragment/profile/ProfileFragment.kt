package com.pbl.mobile.ui.main.fragment.profile

import androidx.fragment.app.activityViewModels
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.databinding.FragmentProfileBinding
import com.pbl.mobile.ui.main.HomeMainViewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding, HomeMainViewModel>() {
    override fun getLazyBinding() = lazy { FragmentProfileBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeMainViewModel>()

    override fun setupInit() {

    }
}
