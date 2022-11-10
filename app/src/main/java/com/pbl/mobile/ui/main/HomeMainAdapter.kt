package com.pbl.mobile.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pbl.mobile.ui.main.fragment.FragmentHome
import com.pbl.mobile.ui.main.fragment.FragmentMyCourse

class HomeMainAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    companion object {
        private const val MAIN_MENU_ITEM_COUNT = 3
        const val HOME_POS = 0
        const val UPLOAD_POS = 1
        const val MY_COURSE_POS = 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            HOME_POS -> FragmentHome()
            MY_COURSE_POS -> FragmentMyCourse()
            else -> {FragmentHome()}
        }
    }

    override fun getItemCount() = MAIN_MENU_ITEM_COUNT
}
