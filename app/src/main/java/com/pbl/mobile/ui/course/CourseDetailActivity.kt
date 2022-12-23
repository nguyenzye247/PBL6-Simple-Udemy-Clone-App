package com.pbl.mobile.ui.course

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pbl.mobile.R
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.ViewModelProviderFactory
import com.pbl.mobile.common.*
import com.pbl.mobile.databinding.ActivityCourseDetailBinding
import com.pbl.mobile.extension.getBaseConfig
import com.pbl.mobile.extension.parcelable
import com.pbl.mobile.extension.showToast
import com.pbl.mobile.model.local.Category
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.model.local.Lecture
import com.pbl.mobile.model.local.Section
import com.pbl.mobile.ui.course.lecture.SectionLectureBottomSheet
import com.pbl.mobile.ui.course.section.SectionAdapter
import com.pbl.mobile.ui.watchlecture.WatchLectureActivity
import com.pbl.mobile.util.HtmlUtils
import com.pbl.mobile.widget.CourseEnrollConfirmDialog


class CourseDetailActivity : BaseActivity<ActivityCourseDetailBinding, CourseDetailsViewModel>(),
    SectionLectureBottomSheet.OnLectureItemSelectListener,
    CourseEnrollConfirmDialog.OnDialogConfirmListener {
    private val categories: ArrayList<Category> = arrayListOf()
    private val sections: ArrayList<Section> = arrayListOf()
    private lateinit var sectionAdapter: SectionAdapter
    private var categoryName: String? = null
    private var isCoursePurchased: Boolean = false
    private var isShowPurchaseAnimation = false

    companion object {
        private const val CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome"
    }

    private val chromeCustomTabLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult? ->
        //TODO: check for purchase in server
        isShowPurchaseAnimation = true
        viewModel.loadMyPurchasedCourseIds()
    }

    override fun getLazyBinding() = lazy { ActivityCourseDetailBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<CourseDetailsViewModel> {
        ViewModelProviderFactory(
            BaseInput.CourseDetailInput(
                application,
                intent.getBooleanExtra(IS_PURCHASED_COURSES_KEY, false)
            )
        )
    }

    override fun setupInit() {
        val course = intent.parcelable<Course>(COURSE_KEY)
        isCoursePurchased = intent.getBooleanExtra(IS_PURCHASED_COURSES_KEY, false)
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
                        llDescription.performClick()
                    }
                    llDescription.setOnClickListener {
                        showCourseDescriptionBottomSheet(course)
                    }
                }
                btnEnrollCourse.setOnClickListener {
                    showEnrollConfirmDialog()
                }
            }
        }
    }

    private fun observe() {
        val course = intent.parcelable<Course>(COURSE_KEY)
        viewModel.myPurchasedCourses().observe(this@CourseDetailActivity) { courseIds ->
            course?.let { currentCourse ->
                if (courseIds.contains(currentCourse.id)) {
                    binding.btnEnrollCourse.text = getString(R.string.purchased)
                    binding.btnEnrollCourse.isEnabled = false
                    isCoursePurchased = true
                    if (isShowPurchaseAnimation) {
                        binding.frSuccessPurchase.isVisible = true
                        binding.aniSuccessPurchase.playAnimation()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.frSuccessPurchase.isVisible = false
                        }, 4000)
                    }
                } else {
                    binding.btnEnrollCourse.isEnabled = true
                    isCoursePurchased = false
                    if (isShowPurchaseAnimation) {
                        binding.frFailPurchase.isVisible = true
                        binding.aniFailPurchase.playAnimation()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.frFailPurchase.isVisible = false
                        }, 4000)
                    }
                }
            }
        }
        viewModel.getInstructor(course?.userId ?: EMPTY_TEXT)
        viewModel.instructor().observe(this@CourseDetailActivity) { response ->
            when (response) {
                is BaseResponse.Success -> {
                    response.data?.let { instructorResponse ->
                        binding.contentLayout.tvCreatedByPerson.text =
                            instructorResponse.data.fullName
                    }
                }
                is BaseResponse.Error -> {
                    binding.contentLayout.tvCreatedByPerson.text = getString(R.string.unknown)
                }
                else -> {
                    // no-ops
                }
            }
        }
        viewModel.getCategories()
        viewModel.categories().observe(this@CourseDetailActivity) { response ->
            when (response) {
                is BaseResponse.Success -> {
                    response.data?.let { categoryResponse ->
                        categories.clear()
                        categories.addAll(categoryResponse.categories)
                        val category = categories.firstOrNull {
                            course?.categoryTopicId == it.id
                        }?.name ?: EMPTY_TEXT
                        binding.contentLayout.tvCourseCategory.text = category
                        categoryName = category
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
                    binding.contentLayout.progressBarCourseContent.isVisible = false
                }
                is BaseResponse.Loading -> {
                    binding.contentLayout.progressBarCourseContent.isVisible = true
                }
            }
        }
        viewModel.isAllLectureLoaded().observe(this@CourseDetailActivity) { isAllLoaded ->
            if (isAllLoaded)
                binding.contentLayout.progressBarCourseContent.isVisible = false
        }
        viewModel.purchaseCourseResponse().observe(this@CourseDetailActivity) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    binding.contentLayout.progressBar.isVisible = true
                }
                is BaseResponse.Success -> {
                    response.data?.let { purchaseResponse ->
                        val purchaseUrl = purchaseResponse.data.paymentUrl
                        launchChromeCustomTab(purchaseUrl)
                    }
                    binding.contentLayout.progressBar.isVisible = false
                }
                is BaseResponse.Error -> {
                    showToast(getString(R.string.error_purchasing_course))
                    binding.contentLayout.progressBar.isVisible = false
                }
            }
        }
    }

    private fun launchChromeCustomTab(paymentUrl: String) {
        CustomTabsClient.bindCustomTabsService(
            this@CourseDetailActivity,
            CUSTOM_TAB_PACKAGE_NAME,
            object : CustomTabsServiceConnection() {
                override fun onCustomTabsServiceConnected(
                    name: ComponentName,
                    client: CustomTabsClient
                ) {
                    client.warmup(0L)
                }

                override fun onServiceDisconnected(name: ComponentName) {

                }
            }
        )
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
        customTabsIntent.intent.data = Uri.parse(paymentUrl)
//        customTabsIntent.launchUrl(this@CourseDetailActivity, Uri.parse(paymentUrl))
        chromeCustomTabLauncher.launch(customTabsIntent.intent)
    }

    private fun showCourseDescriptionBottomSheet(course: Course) {
        val courseDescriptionBottomSheet =
            CourseDescriptionBottomSheet.newInstance(course)
        courseDescriptionBottomSheet.show(supportFragmentManager, CourseDescriptionBottomSheet.TAG)
    }

    private fun showSectionLecturesBottomSheet(sectionName: String, lectures: ArrayList<Lecture>) {
        val lecturesBottomSheet =
            SectionLectureBottomSheet.newInstance(
                sectionName,
                lectures,
                isCoursePurchased
            )
        lecturesBottomSheet.show(supportFragmentManager, SectionLectureBottomSheet.TAG)
    }

    override fun onLectureItemSelect(lecture: Lecture, lectures: ArrayList<Lecture>) {
        goToWatchLecture(lecture, lectures)
    }

    private fun goToWatchLecture(lecture: Lecture, lectures: ArrayList<Lecture>) {
        startActivity(
            Intent(this@CourseDetailActivity, WatchLectureActivity::class.java).apply {
                putExtra(LECTURE_KEY, lecture)
                putExtra(CATEGORY_KEY, categoryName)
                putExtra(IS_PURCHASED_COURSES_KEY, isCoursePurchased)
                putParcelableArrayListExtra(LIST_LECTURE_KEY, lectures)
            }
        )
    }

    private fun showEnrollConfirmDialog() {
        val enrollConfirmDialog = CourseEnrollConfirmDialog.newInstance()
        enrollConfirmDialog.show(supportFragmentManager, CourseEnrollConfirmDialog.TAG)
    }

    override fun onEnrollConfirm() {
        val course = intent.parcelable<Course>(COURSE_KEY)
        viewModel.makePurchase(
            course?.id ?: EMPTY_TEXT,
            this.getBaseConfig().myId
        )
    }
}
