package com.pbl.mobile.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pbl.mobile.ui.main.fragment.holder.HolderFragment
import com.pbl.mobile.ui.main.fragment.home.HomeFragment
import com.pbl.mobile.ui.main.fragment.mycourse.MyCourseFragment
import com.pbl.mobile.ui.main.fragment.profile.ProfileFragment

class HomeMainAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    companion object {
        private const val MAIN_MENU_ITEM_COUNT = 5
        const val HOME_POS = 0
        const val HOLDER_POS = 1
        const val UPLOAD_POS = 2
        const val MY_COURSE_POS = 3
        const val PROFILE_POS = 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            HOME_POS -> HomeFragment()
            HOLDER_POS -> HolderFragment()
            MY_COURSE_POS -> MyCourseFragment()
            PROFILE_POS -> ProfileFragment()
            else -> {
                HomeFragment()
            }
        }
    }

    override fun getItemCount() = MAIN_MENU_ITEM_COUNT
}
