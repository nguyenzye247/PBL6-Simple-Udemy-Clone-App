package com.pbl.mobile.ui.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.MenuItem
import android.view.WindowInsets
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.pbl.mobile.R
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.ViewModelProviderFactory
import com.pbl.mobile.common.COURSE_KEY
import com.pbl.mobile.common.IS_PURCHASED_COURSES_KEY
import com.pbl.mobile.databinding.ActivitySearchBinding
import com.pbl.mobile.extension.observeOnUiThread
import com.pbl.mobile.extension.setMarginTop
import com.pbl.mobile.helper.RxSearch
import com.pbl.mobile.model.dto.CategoryTagItem
import com.pbl.mobile.model.dto.FilterData
import com.pbl.mobile.model.local.Category
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.ui.course.CourseDetailActivity
import com.pbl.mobile.ui.main.fragment.home.HomeCourseAdapter
import com.pbl.mobile.widget.SearchFilterDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SearchActivity : BaseActivity<ActivitySearchBinding, SearchViewModel>() {
    private val categories: ArrayList<CategoryTagItem> = arrayListOf()
    private val tags: ArrayList<CategoryTagItem> = arrayListOf()
    private var filterData: FilterData? = null
    private val purchasedCourseIds: ArrayList<String> = arrayListOf()
    private val loadedInstructorIds = mutableSetOf<String>()
    private lateinit var searchCourseAdapter: HomeCourseAdapter

    companion object;

    override fun getLazyBinding() = lazy { ActivitySearchBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<SearchViewModel> {
        ViewModelProviderFactory(
            BaseInput.SearchInput(
                application
            )
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setupInit() {
        initActionBar()
        initContentMarginRelativeToParent()
        initViews()
        initListeners()
        observe()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbarSearch)
        val actionBar = supportActionBar
        actionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_back)
        }
    }

    private fun initContentMarginRelativeToParent() {
        makeStatusBarTransparent()
        ViewCompat.setOnApplyWindowInsetsListener(
            binding.toolbarSearch
        ) { v, insets ->
            @SuppressLint("WrongConstant")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val marginTop = insets.getInsets(WindowInsets.Type.statusBars()).top
                binding.toolbarSearch.setMarginTop(marginTop)
            }
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                val marginTop = insets.systemWindowInsetTop
                binding.toolbarSearch.setMarginTop(marginTop)
            }

            insets
        }
    }

    private fun initViews() {
        binding.apply {
            rvSearchResult.apply {
                searchCourseAdapter = HomeCourseAdapter(
                    onCourseItemClickCallback = { course ->
                        goToCourseDetail(course)
                    }
                )
                adapter = searchCourseAdapter
                layoutManager = LinearLayoutManager(
                    this@SearchActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            btnFilter.setOnClickListener {
                showFilterDialog()
            }
        }
    }

    private fun observe() {
        subscription.add(
            RxSearch.fromView(binding.etSearch)
                .observeOnUiThread()
                .debounce(600, TimeUnit.MILLISECONDS)
                .subscribe { searchString ->
                    //TODO: perform search
                    filterData?.let { filter ->
                        performSearch(filter)
                    } ?: kotlin.run {
                        lifecycleScope.launch {
                            loadedInstructorIds.clear()
                            viewModel.searchCourse(
                                searchString,
                                0, 500000L,
                                "",
                                ""
                            ).collectLatest {
                                searchCourseAdapter.submitData(it)
                            }
                        }
                    }
                }
        )
        viewModel.apply {
            categories().observe(this@SearchActivity) { response ->
                when (response) {
                    is BaseResponse.Loading -> {

                    }
                    is BaseResponse.Success -> {
                        response.data?.let { categoryResponse ->
                            categories.clear()
                            categories.addAll(
                                categoryResponse.categories.map {
                                    CategoryTagItem(
                                        it.name,
                                        it.id,
                                        isCategory = true,
                                        isSelected = false
                                    )
                                }
                            )
                            searchCourseAdapter.setCategories(categoryResponse.categories as ArrayList<Category>)
                        }
                    }
                    is BaseResponse.Error -> {

                    }
                }
            }
            hashtag().observe(this@SearchActivity) { response ->
                when (response) {
                    is BaseResponse.Loading -> {

                    }
                    is BaseResponse.Success -> {
                        response.data?.let { hashtagResponse ->
                            tags.clear()
                            tags.addAll(
                                hashtagResponse.hashtags.map {
                                    CategoryTagItem(
                                        it.name,
                                        it.id,
                                        isCategory = false,
                                        isSelected = false
                                    )
                                }
                            )
                        }
                    }
                    is BaseResponse.Error -> {

                    }
                }
            }
            viewModel.isFinishedLoadSearchInstructor().observe(this@SearchActivity) { isFinished ->
                if (isFinished) {
                    searchCourseAdapter.setInstructor(viewModel.homeInstructors)
                }
            }
            viewModel.isFinishedLoadMyPurchaseCourses().observe(this@SearchActivity) { isFinished ->
                if (isFinished) {
                    purchasedCourseIds.clear()
                    purchasedCourseIds.addAll(viewModel.myPurchaseCourses.map { it.id })
                }
            }
            lifecycleScope.launch {
                searchCourseAdapter.loadStateFlow
                    .distinctUntilChangedBy { it.append is LoadState.NotLoading }
                    .collect { _ ->
                        val list = searchCourseAdapter.snapshot().items
                        if (list.isNotEmpty()) {
                            val courseUserIds = list.map { it.userId }
                            courseUserIds.forEach { id ->
                                loadedInstructorIds.add(id)
                            }
                            if (loadedInstructorIds.isNotEmpty()) {
                                viewModel.loadSearchInstructors(loadedInstructorIds)
                            }
                        }
                    }
            }
        }
    }

    private fun showFilterDialog() {
        val filterDialog = SearchFilterDialog.newInstance(
            onFilterApplyListener = { filterData ->
                performSearch(filterData)
                Log.d("OKEOKE", filterData.toString())
            },
            categories,
            tags
        )
        filterDialog.show(supportFragmentManager, SearchFilterDialog.TAG)
    }

    private fun performSearch(filterData: FilterData) {
        loadedInstructorIds.clear()
        this.filterData = filterData
        val catString = filterData.categories.map { it.name }
        val tagString = filterData.tags.map { it.name }
        lifecycleScope.launch {
            viewModel.searchCourse(
                binding.etSearch.text.toString(),
                filterData.minPrice, filterData.maxPrice,
                catString.joinToString(separator = ","),
                tagString.joinToString(separator = ",")
            ).collectLatest {
                searchCourseAdapter.submitData(it)
            }
        }
    }

    private fun goToCourseDetail(course: Course?) {
        Intent(this@SearchActivity, CourseDetailActivity::class.java).apply {
            putExtra(COURSE_KEY, course)
            putExtra(IS_PURCHASED_COURSES_KEY, purchasedCourseIds.contains(course?.id))
            startActivity(this)
        }
    }
}