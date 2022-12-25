package com.pbl.mobile.ui.editprofile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pbl.mobile.ui.editprofile.profile.EditProfileFragment
import com.pbl.mobile.ui.editprofile.password.ChangePasswordFragment

class EditProfilePagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
        companion object {
            const val ITEM_COUNT = 2
            const val EDIT_PROFILE_INDEX = 0
            const val CHANGE_PASSWORD_INDEX = 1
        }

        override fun getItemCount() = ITEM_COUNT

        override fun createFragment(position: Int): Fragment {
            return if (position == EDIT_PROFILE_INDEX) {
                EditProfileFragment.newInstance()
            } else {
                ChangePasswordFragment.newInstance()
            }
        }
}
