package com.pbl.mobile.ui.upload

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pbl.mobile.ui.upload.fragment.UploadInformationFragment
import com.pbl.mobile.ui.upload.fragment.UploadSectionFragment

class UploadAdapter(
    private val fragmentActivity: FragmentManager,
    private val lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentActivity, lifecycle) {

    companion object {
        const val UPLOAD_INFO_POS = 0
        const val UPLOAD_SECTION_POS = 1
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            UPLOAD_INFO_POS -> {
                UploadInformationFragment()
            }
            UPLOAD_SECTION_POS -> {
                UploadSectionFragment()
            }
            else -> {
                UploadInformationFragment()
            }
        }
    }

    override fun getItemCount() = 2
}
