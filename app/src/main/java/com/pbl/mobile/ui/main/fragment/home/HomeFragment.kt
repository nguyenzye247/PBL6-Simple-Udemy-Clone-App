package com.pbl.mobile.ui.main.fragment.home

import android.content.Intent
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.common.COURSE_KEY
import com.pbl.mobile.databinding.FragmentHomeBinding
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.ui.course.CourseDetailActivity
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
            homeCourseAdapter = HomeCourseAdapter(
                subscription,
                onCourseItemClickCallback = {
                    goToCourseDetail(it)
                }
            )
            binding.apply {
                rvCourses.adapter = homeCourseAdapter
                rvCourses.layoutManager = linearLayoutManager
            }
        }
    }

    private fun initListener() {
        binding.apply {
            homeCourseAdapter.addLoadStateListener { loadStates ->
                if (loadStates.refresh !is LoadState.Loading)
                    progressBar.isVisible = false
            }
        }
    }

    private fun observe() {
        viewModel.getCourses().observe(this@HomeFragment) {
            homeCourseAdapter.submitData(lifecycle, it)
        }
    }

    private fun goToCourseDetail(course: Course?) {
        Intent(context, CourseDetailActivity::class.java).apply {
            putExtra(COURSE_KEY, course)
            startActivity(this)
        }
    }
}
