package com.pbl.mobile.ui.main.fragment.mycourse

import android.content.Intent
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.common.COURSE_KEY
import com.pbl.mobile.common.IS_PURCHASED_COURSES_KEY
import com.pbl.mobile.databinding.FragmentMyCourseBinding
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.ui.course.CourseDetailActivity
import com.pbl.mobile.ui.main.HomeMainViewModel

class MyPurchasedCourseFragment : BaseFragment<FragmentMyCourseBinding, HomeMainViewModel>() {
    private val purchasedCourses: ArrayList<Course> = arrayListOf()
    private val purchasedInstructorIds = mutableSetOf<String>()
    private lateinit var purchasedCourseAdapter: MyPurchasedCourseAdapter

    override fun getLazyBinding() = lazy { FragmentMyCourseBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeMainViewModel>()

    override fun setupInit() {
        initViews()
        initListeners()
        observe()
    }

    private fun initViews() {
        binding.apply {
            rvMyCourse.apply {
                purchasedCourseAdapter = MyPurchasedCourseAdapter(
                    purchasedCourses,
                    onCourseItemClickCallback = { course ->
                        goToCourseDetail(course)
                    }
                )
                adapter = purchasedCourseAdapter
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }
        }
    }

    private fun initListeners() {
        binding.apply {

        }
    }

    private fun observe() {
        viewModel.apply {
            isFinishedLoadMyPurchaseCourses().observe(this@MyPurchasedCourseFragment) { isFinished ->
                if (isFinished) {
                    purchasedCourses.clear()
                    purchasedCourses.addAll(viewModel.myPurchaseCourses)
                    purchasedCourseAdapter.notifyDataSetChanged()
                    purchasedCourses.forEach { purchasedInstructorIds.add(it.userId) }
                    if (purchasedInstructorIds.isNotEmpty())
                        loadMyPurchasedInstructor(purchasedInstructorIds)
                }
            }
            loadMyPurchasedCourseIds()
            isFinishedLoadPurchasedInstructor().observe(this@MyPurchasedCourseFragment) { isFinished ->
                if(isFinished)
                    purchasedCourseAdapter.setInstructor(viewModel.purchasedInstructors)
            }
        }
    }

    private fun goToCourseDetail(course: Course?) {
        Intent(context, CourseDetailActivity::class.java).apply {
            putExtra(COURSE_KEY, course)
            putExtra(IS_PURCHASED_COURSES_KEY, true)
            startActivity(this)
        }
    }
}