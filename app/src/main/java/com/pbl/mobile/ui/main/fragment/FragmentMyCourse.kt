package com.pbl.mobile.ui.main.fragment

import androidx.fragment.app.activityViewModels
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.databinding.FragmentMyCourseBinding
import com.pbl.mobile.ui.main.HomeMainViewModel

class FragmentMyCourse : BaseFragment<FragmentMyCourseBinding, HomeMainViewModel>() {
    override fun getLazyBinding() = lazy { FragmentMyCourseBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeMainViewModel>()

    override fun setupInit() {

    }
}