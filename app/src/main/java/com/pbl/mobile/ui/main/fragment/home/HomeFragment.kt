package com.pbl.mobile.ui.main.fragment.home

import android.content.Intent
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.common.COURSE_KEY
import com.pbl.mobile.databinding.FragmentHomeBinding
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.model.remote.user.GetSimpleUserResponse
import com.pbl.mobile.ui.course.CourseDetailActivity
import com.pbl.mobile.ui.main.HomeMainViewModel
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeMainViewModel>() {
    private lateinit var homeCourseAdapter: HomeCourseAdapter
    private val loadedInstructorIds = arrayListOf<String>()

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
                .distinctUntilChangedBy { it.refresh }
                .collect { _ ->
                    val list = homeCourseAdapter.snapshot().items
                    if (list.isNotEmpty()) {
                        val courseUserIds = list.map { it.userId }
                        val unloadedUserIds = arrayListOf<String>()
                        courseUserIds.forEach { id ->
                            if (!loadedInstructorIds.contains(id)) {
                                unloadedUserIds.add(id)
                                loadedInstructorIds.add(id)
                            }
                        }
                        if (unloadedUserIds.isNotEmpty()) {
                            viewModel.loadInstructors(unloadedUserIds)
                        }
                    }
                }
        }
        viewModel.isFinishedLoadInstructor().observe(this@HomeFragment) { isFinished ->
            if (isFinished) {
                homeCourseAdapter.setInstructor(viewModel.instructors)
            }
        }
        viewModel.getCourses().observe(this@HomeFragment) { pagingCourseData ->
            homeCourseAdapter.submitData(lifecycle, pagingCourseData)
        }
    }

    private fun goToCourseDetail(course: Course?) {
        Intent(context, CourseDetailActivity::class.java).apply {
            putExtra(COURSE_KEY, course)
            startActivity(this)
        }
    }
}
