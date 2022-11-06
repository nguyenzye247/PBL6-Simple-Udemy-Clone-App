package com.pbl.mobile.ui.home

import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.pbl.mobile.R
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.ViewModelProviderFactory
import com.pbl.mobile.databinding.ActivityHomeBinding
import com.pbl.mobile.databinding.HeaderHomeNavBinding


class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>() {
    private val navController by lazy { findNavController(R.id.nav_host_fragment_home_container) }

    private val navHeaderBinding by lazy {
        HeaderHomeNavBinding.bind(
            binding.navHomeView.getHeaderView(
                0
            )
        )
    }
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun getLazyBinding() = lazy { ActivityHomeBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<HomeViewModel> {
        ViewModelProviderFactory(BaseInput.MainInput(application))
    }

    override fun setupInit() {
        setUpNavigationController()
        initViews()
        initListeners()
    }

    private fun setUpNavigationController() {
        initDummyActionbar()
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_popular_course, R.id.nav_trendy
            ), binding.mainDrawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navHomeView.setupWithNavController(navController)
    }

    private fun initDummyActionbar() {
        setSupportActionBar(binding.layoutAppBarHome.homeToolbar)
        val actionBar = supportActionBar
        actionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(false)
            setVisible(false)
        }
    }

    private fun initViews() {
        binding.apply {

        }
    }

    private fun initListeners() {
        initNavigationListener()
        binding.apply {
            layoutAppBarHome.ivNavigationMenu.setOnClickListener { openNavigation() }
        }
        navHeaderBinding.ivCloseNav.setOnClickListener { closeNavigation() }
    }

    private fun initNavigationListener() {
        var lastCheck: MenuItem = binding.navHomeView.menu.findItem(R.id.nav_home)
        binding.apply {
            navHomeView.setNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> {
                        lastCheck.isChecked = false
                        lastCheck = navHomeView.menu.findItem(R.id.nav_home)
                        Log.d("MENU-11", "nav_home_page: ")
                        navController.navigate(R.id.nav_home)
                        changeHomeBackground(true)
                        closeNavigation()
                    }
                    R.id.nav_popular_course -> {
                        lastCheck.isChecked = false
                        lastCheck = navHomeView.menu.findItem(R.id.nav_popular_course)
                        Log.d("MENU-11", "nav_popular_course: ")
                        navController.navigate(R.id.nav_popular_course)
                        changeHomeBackground(true)
                        closeNavigation()
                    }
                    R.id.nav_trendy -> {
                        lastCheck.isChecked = false
                        lastCheck = navHomeView.menu.findItem(R.id.nav_trendy)
                        Log.d("MENU-11", "nav_trendy: ")
                        navController.navigate(R.id.nav_trendy)
                        changeHomeBackground(true)
                        closeNavigation()
                    }
                }
                true
            }
            layoutAppBarHome.ivUpload.setOnClickListener {
                changeHomeBackground(false)
                navController.navigate(R.id.nav_upload, null, getNavOpitons())
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_home_container)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun openNavigation() {
        binding.mainDrawerLayout.openDrawer(GravityCompat.START)
    }

    private fun closeNavigation() {
        binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun getNavOpitons(): NavOptions {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.slide_up)
//            .setExitAnim(R.anim.default_exit_anim)
//            .setPopEnterAnim(R.anim.default_pop_enter_anim)
//            .setPopExitAnim(R.anim.default_pop_exit_anim)
            .build()
    }

    private fun changeHomeBackground(isMain: Boolean) {
        if (isMain) {
            binding.layoutAppBarHome.ivTopBackground.setImageDrawable(
                ContextCompat.getDrawable(
                    this@HomeActivity,
                    R.drawable.background_yellow_top
                )
            )
            binding.layoutAppBarHome.root.setBackgroundColor(
                ContextCompat.getColor(
                    this@HomeActivity,
                    R.color.white
                )
            )
        } else {
            binding.layoutAppBarHome.ivTopBackground.setImageDrawable(
                ContextCompat.getDrawable(
                    this@HomeActivity,
                    R.drawable.background_dim_yellow_top
                )
            )
            binding.layoutAppBarHome.root.setBackgroundColor(
                ContextCompat.getColor(
                    this@HomeActivity,
                    R.color.white_100
                )
            )
        }
    }
}