package com.pbl.mobile.ui.main.fragment.holder

import androidx.fragment.app.activityViewModels
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.databinding.FragmentHolderBinding
import com.pbl.mobile.ui.main.HomeMainViewModel

class HolderFragment : BaseFragment<FragmentHolderBinding, HomeMainViewModel>() {
    override fun getLazyBinding() = lazy { FragmentHolderBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeMainViewModel>()

    override fun setupInit() {

    }
}
