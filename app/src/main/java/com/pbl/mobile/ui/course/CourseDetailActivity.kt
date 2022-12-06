package com.pbl.mobile.ui.course

import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pbl.mobile.R
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.ViewModelProviderFactory
import com.pbl.mobile.common.COURSE_KEY
import com.pbl.mobile.databinding.ActivityCourseDetailBinding
import com.pbl.mobile.extension.parcelable
import com.pbl.mobile.extension.showToast
import com.pbl.mobile.model.local.Category
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.model.local.Lecture
import com.pbl.mobile.model.local.Section
import com.pbl.mobile.ui.course.lecture.SectionLectureBottomSheet
import com.pbl.mobile.ui.course.section.SectionAdapter
import com.pbl.mobile.util.HtmlUtils

class CourseDetailActivity : BaseActivity<ActivityCourseDetailBinding, CourseViewModel>(),
    SectionLectureBottomSheet.OnLectureItemSelectListener {
    private val categories = arrayListOf<Category>()
    private val sections = arrayListOf<Section>()
    private val lectures = arrayListOf<Lecture>()
    private lateinit var sectionAdapter: SectionAdapter

    override fun getLazyBinding() = lazy { ActivityCourseDetailBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<CourseViewModel> {
        ViewModelProviderFactory(
            BaseInput.CourseDetailInput(
                application
            )
        )
    }

    override fun setupInit() {
        val course = intent.parcelable<Course>(COURSE_KEY)
        initViews(course)
        initListener(course)
        observe()
        initActionBar()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbCourse)
        val actionBar = supportActionBar
        actionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_back)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews(course: Course?) {
        binding.apply {
            course?.let { courseData ->
                Glide.with(this@CourseDetailActivity)
                    .load(courseData.thumbnailUrl)
                    .into(ivCourseThumbnail)
                collapseToolbarLayout.title =
                    categories.firstOrNull {
                        courseData.categoryTopicId == it.id
                    }?.name ?: ""
            }
        }
        binding.contentLayout.apply {
            course?.let { courseDetail ->
                "$${courseDetail.price.toFloat().toInt()}".also { tvCoursePrice.text = it }
                tvCourseTitle.text = courseDetail.name
                tvCourseDescription.text =
                    HtmlUtils.fromHtmlText(HtmlUtils.removeEndlines(courseDetail.description))
                ivNoCourseContent.isVisible = false
            }
            sectionAdapter = SectionAdapter(
                sections,
                onSectionClickCallback = { section ->
                    viewModel.lectures()[section.id]?.let {
                        showSectionLecturesBottomSheet(
                            section.name,
                            it
                        )
                    }
                }
            )
            rvCourseSections.adapter = sectionAdapter
            rvCourseSections.layoutManager = LinearLayoutManager(
                this@CourseDetailActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        }
    }

    private fun initListener(course: Course?) {
        course?.let {
            binding.apply {
                contentLayout.apply {
                    tvCourseDescription.setOnClickListener {
                        showCourseDescriptionBottomSheet(course)
                    }
                }
                btnEnrollCourse.setOnClickListener {
                    //TODO: confirm enroll course
                }
            }
        }
    }

    private fun observe() {
        val course = intent.parcelable<Course>(COURSE_KEY)
        viewModel.getCategories()
        viewModel.categories().observe(this@CourseDetailActivity) { response ->
            when (response) {
                is BaseResponse.Success -> {
                    response.data?.let { categoryResponse ->
                        categories.clear()
                        categories.addAll(categoryResponse.categories)
                        binding.contentLayout.tvCourseCategory.text =
                            categories.firstOrNull {
                                course?.categoryTopicId == it.id
                            }?.name ?: ""
                    }
                }
                is BaseResponse.Error -> {

                }
                is BaseResponse.Loading -> {

                }
            }
        }
        viewModel.getSections(course?.id ?: "")
        viewModel.courseSections().observe(this@CourseDetailActivity) { response ->
            when (response) {
                is BaseResponse.Success -> {
                    response.data?.let { sectionResponse ->
                        sectionResponse.data.sections.let {
                            sections.clear()
                            sections.addAll(it)
                            sectionAdapter.notifyDataSetChanged()
                            binding.contentLayout.ivNoCourseContent.isVisible = sections.isEmpty()
                            binding.tvSectionCount.text = it.size.toString()
//                            binding.tvOnDemandHours.text =
                        }
//                        binding.contentLayout.progressBar.isVisible = false
                    }
                }
                is BaseResponse.Error -> {
                    showToast("Error Loading Course Contents")
                    binding.contentLayout.progressBar.isVisible = false
                }
                is BaseResponse.Loading -> {
                    binding.contentLayout.progressBar.isVisible = true
                }
            }
        }
        viewModel.isAllLectureLoaded().observe(this@CourseDetailActivity) { isAllLoaded ->
            if (isAllLoaded)
                binding.contentLayout.progressBar.isVisible = false
        }
    }

    private fun showCourseDescriptionBottomSheet(course: Course) {
        val courseDescriptionBottomSheet =
            CourseDescriptionBottomSheet.newInstance(course)
        courseDescriptionBottomSheet.show(supportFragmentManager, CourseDescriptionBottomSheet.TAG)
    }

    private fun showSectionLecturesBottomSheet(sectionName: String, lectures: ArrayList<Lecture>) {
        val lecturesBottomSheet = SectionLectureBottomSheet.newInstance(sectionName, lectures)
        lecturesBottomSheet.show(supportFragmentManager, SectionLectureBottomSheet.TAG)
    }

    override fun onLectureItemSelect(lecture: Lecture) {
        goToWatchLecture()
    }

    private fun goToWatchLecture() {
        showToast("Watch Video")
    }
}
