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
import com.pbl.mobile.extension.showToast
import com.pbl.mobile.ui.upload.UploadActivity
import com.pbl.mobile.ui.upload.dialog.UploadBottomSheet
import io.sentry.Sentry


class HomeMainActivity : BaseActivity<ActivityHomeBinding, HomeMainViewModel>(),
    OnUploadOptionSelect {
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
            R.id.menu_notification -> {

            }
            R.id.menu_language -> {

            }
            R.id.menu_search -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setupInit() {
//        try {
//            throw Exception("Lifecycle error test.")
//        } catch (e: Exception) {
//            Sentry.captureException(e)
//        }
        initViews()
        initListeners()
        observe()
    }

    private fun initViews() {
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
        viewModel
    }

    private fun initNavigationListener() {
        binding.apply {

        }
    }

    private fun initBottomNavigation() {
        binding.bottomNavMain.apply {
            this.setOnItemSelectedListener { menuItem ->
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

    override fun onUploadCourseSelect() {
//        goToUploadCourse()
    }

    override fun onBackPressed() {
        //TODO: Show dialog exit confirmation
    }
}
