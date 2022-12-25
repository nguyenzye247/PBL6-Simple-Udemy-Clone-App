package com.pbl.mobile.ui.main.fragment.profile

import android.content.Context
import android.content.Intent
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pbl.mobile.R
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.common.COURSE_KEY
import com.pbl.mobile.common.EMPTY_SPACE
import com.pbl.mobile.common.EMPTY_TEXT
import com.pbl.mobile.common.IS_PURCHASED_COURSES_KEY
import com.pbl.mobile.databinding.FragmentProfileBinding
import com.pbl.mobile.extension.getBaseConfig
import com.pbl.mobile.extension.showToast
import com.pbl.mobile.model.Role
import com.pbl.mobile.model.local.Category
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.model.remote.user.GetSimpleUserResponse
import com.pbl.mobile.ui.course.CourseDetailActivity
import com.pbl.mobile.ui.main.HomeMainViewModel
import com.pbl.mobile.util.DateFormatUtils
import com.pbl.mobile.widget.InstructorRequestConfirmDialog
import com.pbl.mobile.widget.SignOutConfirmDialog
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch

class ProfileFragment : BaseFragment<FragmentProfileBinding, HomeMainViewModel>() {
    private lateinit var myCourseAdapter: MyCoursesAdapter
    private val categories: ArrayList<Category> = arrayListOf()
    private lateinit var onEditProfileClickListener: OnEditProfileClickListener

    override fun getLazyBinding() = lazy { FragmentProfileBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeMainViewModel>()

    override fun setupInit() {
        initViews()
        initListeners()
        observe()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditProfileClickListener) {
            onEditProfileClickListener = context
        }
    }

    private fun initViews() {
        binding.apply {
            context?.let { context ->
                val avatarUrl = context.getBaseConfig().myAvatar
                val role = context.getBaseConfig().myRole
                val name = context.getBaseConfig().fullName
                val id = context.getBaseConfig().myId
                val joinAt = context.getBaseConfig().joinAt

                Glide.with(this@ProfileFragment)
                    .load(avatarUrl)
                    .placeholder(R.drawable.avatar_holder_person)
                    .into(ivUserAvatar)
                tvUserFullName.text = name
                val joinAtText =
                    getString(R.string.join_at) + EMPTY_SPACE + DateFormatUtils.parseTimeZoneDate(
                        joinAt
                    )
                tvJoinAt.text = joinAtText
                //TODO: set true if have statistic implemented
                btnStatistic.isEnabled = false

                rvMyPublishCourse.apply {
                    myCourseAdapter = MyCoursesAdapter(
                        GetSimpleUserResponse.User(
                            EMPTY_TEXT,
                            name,
                            role,
                            id,
                            avatarUrl
                        ),
                        onCourseItemClickCallback = { course ->
                            goToCourseDetail(course)
                        }
                    )
                    adapter = myCourseAdapter
                    layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                }
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            myCourseAdapter.addLoadStateListener { loadStates ->
                if (loadStates.refresh !is LoadState.Loading)
                    progressBarMyCourses.isVisible = false
            }
            btnEditProfile.setOnClickListener {
                goToEditProfile()
            }
            btnMoreInfo.setOnClickListener {
                showDropDownMenu(it as ImageButton)
            }
            btnStatistic.setOnClickListener {

            }
        }
    }

    private fun observe() {
        viewModel.apply {
            categories().observe(this@ProfileFragment) { response ->
                when (response) {
                    is BaseResponse.Success -> {
                        response.data?.let { categoryResponse ->
                            categories.clear()
                            categories.addAll(categoryResponse.categories)
                            myCourseAdapter.setCategories(categories)
                        }
                    }
                    is BaseResponse.Error -> {

                    }
                    is BaseResponse.Loading -> {

                    }
                }
            }
            getInstructorFollowers().observe(this@ProfileFragment) { response ->
                when (response) {
                    is BaseResponse.Success -> {
                        response.data?.let { followerResponse ->
                            binding.tvFollowersCount.text =
                                followerResponse.pagination.totalCount.toString()
                        }
                    }
                    is BaseResponse.Error -> {
                        binding.tvFollowersCount.text = "0"
                    }
                    else -> {
                        //no-ops
                    }
                }
            }
            instructorStudents().observe(this@ProfileFragment) { response ->
                when (response) {
                    is BaseResponse.Success -> {
                        response.data?.let { studentsResponse ->
                            binding.tvStudentCount.text =
                                studentsResponse.pagination.totalCount.toString()
                        }
                    }
                    is BaseResponse.Error -> {
                        binding.tvStudentCount.text = "0"
                    }
                    else -> {
                        //no-ops
                    }
                }
            }
            requestInstructorResponse().observe(this@ProfileFragment) { response ->
                when (response) {
                    is BaseResponse.Success -> {
                        context?.let {
                            it.showToast(getString(R.string.your_request_is_sent))
                        }
                    }
                    is BaseResponse.Error -> {
                        context?.let {
                            it.showToast(
                                mes = getString(R.string.already_request_to_become_instructor),
                                duration = Toast.LENGTH_LONG
                            )
                        }
                    }
                    else -> {
                        //no-ops
                    }
                }
            }
            lifecycleScope.launch {
                myCourseAdapter.loadStateFlow
                    .distinctUntilChangedBy { it.refresh }
                    .collect {
                        val list = myCourseAdapter.snapshot().items
                        if (list.isNotEmpty()) {
                            binding.tvCourseCount.text = list.size.toString()
                            binding.ivEmpty.isVisible = false
                        }
                    }
            }
            getInstructorCourses().observe(this@ProfileFragment) {
                myCourseAdapter.submitData(lifecycle, it)
            }
            loadInstructorStudents()
            loadInstructorFollowers()
        }
    }

    private fun showDropDownMenu(infoButton: ImageButton) {
        context?.let {
            val role = it.getBaseConfig().myRole
            val popupMenu = PopupMenu(it, infoButton)
            popupMenu.inflate(R.menu.menu_more_info)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_instructor_request -> {
                        handleInstructorRequest()
                        true
                    }
                    R.id.menu_sign_out -> {
                        handleSignOut()
                        true
                    }
                    else -> false
                }
            }
            val pMenu = popupMenu.menu
            val instructorRequestMenu = pMenu.findItem(R.id.menu_instructor_request)
            if (role == Role.INSTRUCTOR.roleTitle) {
                instructorRequestMenu.isVisible = false
            }
            popupMenu.show()
        }
    }

    private fun handleInstructorRequest() {
        val instructorRequestConfirmDialog = InstructorRequestConfirmDialog.newInstance()
        instructorRequestConfirmDialog.show(
            parentFragmentManager,
            InstructorRequestConfirmDialog.TAG
        )
    }

    private fun handleSignOut() {
        val signOutConfirmDialog = SignOutConfirmDialog.newInstance()
        signOutConfirmDialog.show(parentFragmentManager, SignOutConfirmDialog.TAG)
    }

    private fun goToCourseDetail(course: Course?) {
        Intent(context, CourseDetailActivity::class.java).apply {
            putExtra(COURSE_KEY, course)
            putExtra(IS_PURCHASED_COURSES_KEY, true)
            startActivity(this)
        }
    }

    private fun goToEditProfile() {
        onEditProfileClickListener.onEditProfileClick()
    }

    interface OnEditProfileClickListener {
        fun onEditProfileClick()
    }
}
