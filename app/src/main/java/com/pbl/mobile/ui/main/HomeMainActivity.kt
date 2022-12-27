package com.pbl.mobile.ui.main

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.pbl.mobile.R
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.ViewModelProviderFactory
import com.pbl.mobile.databinding.ActivityHomeBinding
import com.pbl.mobile.extension.getBaseConfig
import com.pbl.mobile.extension.showToast
import com.pbl.mobile.ui.editprofile.EditProfileActivity
import com.pbl.mobile.ui.main.fragment.profile.ProfileFragment
import com.pbl.mobile.ui.search.SearchActivity
import com.pbl.mobile.ui.signin.SignInActivity
import com.pbl.mobile.ui.upload.UploadActivity
import com.pbl.mobile.ui.upload.dialog.UploadBottomSheet
import com.pbl.mobile.widget.InstructorRequestConfirmDialog
import com.pbl.mobile.widget.SignOutConfirmDialog


class HomeMainActivity : BaseActivity<ActivityHomeBinding, HomeMainViewModel>(),
    OnUploadOptionSelect, ProfileFragment.OnEditProfileClickListener,
    SignOutConfirmDialog.OnSignOutConfirmListener,
    InstructorRequestConfirmDialog.OnInstructorRequestConfirmListener {
    private lateinit var homeMainAdapter: HomeMainAdapter
    private var uploadBottomSheet: UploadBottomSheet? = null

    override fun getLazyBinding() = lazy { ActivityHomeBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<HomeMainViewModel> {
        ViewModelProviderFactory(BaseInput.MainInput(application))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                onBackPressed()
            R.id.menu_notification -> {

            }
            R.id.menu_language -> {

            }
            R.id.menu_search -> {
                goToSearch()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setupInit() {
        initActionBar()
        initViews()
        initListeners()
        observe()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.homeToolbar)
        val actionBar = supportActionBar
        actionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initViews() {
        makeStatusBarTransparent()
        binding.apply {
        }
    }

    private fun initListeners() {
        initViewpager()
        initBottomNavigation()
        initNavigationListener()
        binding.apply {
            btnFabUpload.setOnClickListener {
                showToast("Upcoming Feature")
//                showUploadBottomSheet()
            }
        }
    }

    private fun observe() {
        viewModel.apply {
            getCategories()
        }
    }

    private fun initNavigationListener() {
        binding.apply {

        }
    }

    private fun initBottomNavigation() {
        binding.bottomNavMain.apply {
            setOnItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_home -> {
                        binding.vp2Main.setCurrentItem(HomeMainAdapter.HOME_POS, false)
                    }
                    R.id.menu_my_course -> {
                        binding.vp2Main.setCurrentItem(HomeMainAdapter.MY_COURSE_POS, false)
                    }
                    R.id.menu_user_profile -> {
                        binding.vp2Main.setCurrentItem(HomeMainAdapter.PROFILE_POS, false)
                    }
                }
                true
            }
        }
    }

    private fun initViewpager() {
        binding.vp2Main.apply {
            homeMainAdapter = HomeMainAdapter(supportFragmentManager, lifecycle)
            adapter = homeMainAdapter
            currentItem = HomeMainAdapter.HOME_POS
            offscreenPageLimit = 3
            isUserInputEnabled = false
        }
    }

    private fun showUploadBottomSheet() {
        uploadBottomSheet = UploadBottomSheet.newInstance()
        uploadBottomSheet?.let {
            if (!it.isAdded) {
                it.show(supportFragmentManager, UploadBottomSheet.TAG)
            }
        }
    }

    private fun goToUploadCourse() {
        Intent(this@HomeMainActivity, UploadActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun goToEditProfile() {
        startActivity(
            Intent(
                this@HomeMainActivity,
                EditProfileActivity::class.java
            )
        )
    }

    private fun goToSearch() {
        startActivity(
            Intent(
                this@HomeMainActivity,
                SearchActivity::class.java
            )
        )
    }

    override fun onUploadCourseSelect() {
//        goToUploadCourse()
    }

    override fun onBackPressed() {
        //TODO: Show dialog exit confirmation
    }

    override fun onEditProfileClick() {
        goToEditProfile()
    }

    override fun onSignOutConfirm() {
        getBaseConfig().clearAll()
        startActivity(Intent(this@HomeMainActivity, SignInActivity::class.java))
        finish()
    }

    override fun onInstructorRequestConfirm() {
        viewModel.requestToBecomeInstructor()
    }
}
