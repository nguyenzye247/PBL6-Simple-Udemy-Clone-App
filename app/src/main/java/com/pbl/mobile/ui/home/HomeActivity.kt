package com.pbl.mobile.ui.home

import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.pbl.mobile.R
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.databinding.ActivityHomeBinding
import com.pbl.mobile.databinding.HeaderHomeNavBinding


class HomeActivity : BaseActivity<ActivityHomeBinding>() {
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
                        closeNavigation()
                    }
                    R.id.nav_popular_course -> {
                        lastCheck.isChecked = false
                        lastCheck = navHomeView.menu.findItem(R.id.nav_popular_course)
                        Log.d("MENU-11", "nav_popular_course: ")
                        navController.navigate(R.id.nav_popular_course)
                        closeNavigation()
                    }
                    R.id.nav_trendy -> {
                        lastCheck.isChecked = false
                        lastCheck = navHomeView.menu.findItem(R.id.nav_trendy)
                        Log.d("MENU-11", "nav_trendy: ")
                        navController.navigate(R.id.nav_trendy)
                        closeNavigation()
                    }
                }
                true
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
}