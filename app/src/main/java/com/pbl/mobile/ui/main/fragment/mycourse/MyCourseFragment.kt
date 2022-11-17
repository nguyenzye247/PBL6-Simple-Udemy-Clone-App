package com.pbl.mobile.ui.main.fragment.mycourse

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.databinding.FragmentMyCourseBinding
import com.pbl.mobile.ui.main.HomeMainViewModel

class MyCourseFragment : BaseFragment<FragmentMyCourseBinding, HomeMainViewModel>() {

    override fun getLazyBinding() = lazy { FragmentMyCourseBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeMainViewModel>()

    override fun setupInit() {
        initViews()
        initListeners()
        observe()
    }

    private fun initViews() {

    }

    private fun initListeners() {

    }

    private fun observe() {

    }

}