package com.pbl.mobile.ui.home.fragment

import androidx.fragment.app.viewModels
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.ViewModelProviderFactory
import com.pbl.mobile.databinding.FragmentTrendingBinding
import com.pbl.mobile.ui.home.HomeViewModel

class FragmentTrending : BaseFragment<FragmentTrendingBinding, HomeViewModel>() {
    override fun getLazyBinding() = lazy { FragmentTrendingBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<HomeViewModel> {
        ViewModelProviderFactory(BaseInput.MainInput(activity?.application))
    }

    override fun setupInit() {

    }
}