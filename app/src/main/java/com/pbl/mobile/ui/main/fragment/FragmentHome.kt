package com.pbl.mobile.ui.main.fragment

import androidx.fragment.app.activityViewModels
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.databinding.FragmentHomeBinding
import com.pbl.mobile.ui.main.HomeMainViewModel

class FragmentHome : BaseFragment<FragmentHomeBinding, HomeMainViewModel>() {
    override fun getLazyBinding() = lazy { FragmentHomeBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeMainViewModel>()

    override fun setupInit() {

    }
}