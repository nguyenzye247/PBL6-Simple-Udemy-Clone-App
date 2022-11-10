package com.pbl.mobile.ui.upload.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pbl.mobile.ui.upload.FragmentUploadInformation
import com.pbl.mobile.ui.upload.FragmentUploadSection

class UploadAdapter(
    private val fragmentActivity: FragmentManager,
    private val lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentActivity, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                FragmentUploadInformation()
            }
            1 -> {
                FragmentUploadSection()
            }
            else -> {
                FragmentUploadInformation()
            }
        }
    }

    override fun getItemCount() = 2
}
