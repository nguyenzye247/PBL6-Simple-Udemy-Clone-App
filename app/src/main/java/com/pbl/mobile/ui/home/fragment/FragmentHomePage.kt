package com.pbl.mobile.ui.home.fragment

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.ViewModelProviderFactory
import com.pbl.mobile.databinding.FragmentHomePageBinding
import com.pbl.mobile.ui.home.HomeViewModel

class FragmentHomePage : BaseFragment<FragmentHomePageBinding, HomeViewModel>(){
    override fun getLazyBinding() = lazy {FragmentHomePageBinding.inflate(layoutInflater)}

    override fun getLazyViewModel() = activityViewModels<HomeViewModel>()

    override fun setupInit() {

    }
}