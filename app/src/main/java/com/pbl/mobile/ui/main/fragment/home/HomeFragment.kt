package com.pbl.mobile.ui.main.fragment.home

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.databinding.FragmentHomeBinding
import com.pbl.mobile.ui.main.HomeMainViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeMainViewModel>() {
    private lateinit var homeCourseAdapter: HomeCourseAdapter
    private val linearLayoutManager by lazy {
        LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    override fun getLazyBinding() = lazy { FragmentHomeBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeMainViewModel>()

    override fun setupInit() {
        initViews()
        initListener()
        observe()
    }

    private fun initViews() {
        binding.apply {
            homeCourseAdapter = HomeCourseAdapter()
            binding.apply {
                rvCourses.adapter = homeCourseAdapter
                rvCourses.layoutManager = linearLayoutManager
            }
        }
    }

    private fun initListener() {
        binding.apply {

        }
    }

    private fun observe() {
        viewModel.getCourses().observe(this@HomeFragment) {
            homeCourseAdapter.submitData(lifecycle, it)
        }
    }
}
