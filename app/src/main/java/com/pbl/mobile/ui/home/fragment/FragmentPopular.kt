package com.pbl.mobile.ui.home.fragment

import androidx.fragment.app.viewModels
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.ViewModelProviderFactory
import com.pbl.mobile.databinding.FragmentPopularBinding
import com.pbl.mobile.ui.home.HomeViewModel

class FragmentPopular : BaseFragment<FragmentPopularBinding, HomeViewModel>() {
    override fun getLazyBinding() = lazy { FragmentPopularBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<HomeViewModel> {
        ViewModelProviderFactory(BaseInput.MainInput(activity?.application))
    }

    override fun setupInit() {

    }
}