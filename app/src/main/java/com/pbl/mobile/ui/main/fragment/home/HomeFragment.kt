package com.pbl.mobile.ui.main.fragment.home

import android.content.Intent
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.common.COURSE_KEY
import com.pbl.mobile.common.IS_PURCHASED_COURSES_KEY
import com.pbl.mobile.databinding.FragmentHomeBinding
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.ui.course.CourseDetailActivity
import com.pbl.mobile.ui.main.HomeMainViewModel
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeMainViewModel>() {
    private lateinit var homeCourseAdapter: HomeCourseAdapter
    private val loadedInstructorIds = mutableSetOf<String>()
    private val purchasedCourseIds: ArrayList<String> = arrayListOf()

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
        lifecycleScope.launch {
            homeCourseAdapter.loadStateFlow
                .distinctUntilChangedBy { it.append is LoadState.NotLoading }
                .collect { _ ->
                    val list = homeCourseAdapter.snapshot().items
                    if (list.isNotEmpty()) {
                        val courseUserIds = list.map { it.userId }
                        courseUserIds.forEach { id ->
                            loadedInstructorIds.add(id)
                        }
                        if (loadedInstructorIds.isNotEmpty()) {
                            viewModel.loadHomeInstructors(loadedInstructorIds)
                        }
                    }
                }
        }
        viewModel.isFinishedLoadHomeInstructor().observe(this@HomeFragment) { isFinished ->
            if (isFinished) {
                homeCourseAdapter.setInstructor(viewModel.homeInstructors)
            }
        }
        viewModel.getCourses().observe(this@HomeFragment) { pagingCourseData ->
            homeCourseAdapter.submitData(lifecycle, pagingCourseData)
        }
        viewModel.isFinishedLoadMyPurchaseCourses().observe(this@HomeFragment) { isFinished ->
            if (isFinished) {
                purchasedCourseIds.clear()
                purchasedCourseIds.addAll(viewModel.myPurchaseCourses.map { it.id })
            }
        }
        viewModel.loadMyPurchasedCourseIds()
    }

    private fun goToCourseDetail(course: Course?) {
        Intent(context, CourseDetailActivity::class.java).apply {
            putExtra(COURSE_KEY, course)
            putExtra(IS_PURCHASED_COURSES_KEY, purchasedCourseIds.contains(course?.id))
            startActivity(this)
        }
    }
}
