package com.pbl.mobile.ui.home

import androidx.core.view.GravityCompat
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun getLazyBinding() = lazy { ActivityHomeBinding.inflate(layoutInflater) }

    override fun setupInit() {
        initViews()
        initListeners()
    }

    private fun initViews() {
        binding.apply {

        }
    }

    private fun initListeners() {
        binding.apply {
            ivNavigationMenu.setOnClickListener { openNavigation() }
            ivCloseNav.setOnClickListener { closeNavigation() }
        }
    }

    private fun openNavigation() {
        binding.mainDrawerLayout.openDrawer(GravityCompat.START)
    }

    private fun closeNavigation() {
        binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
    }
}