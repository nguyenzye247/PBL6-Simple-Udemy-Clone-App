package com.pbl.mobile.widget

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout


class AppBarTransparentScrollingViewBehavior : AppBarLayout.ScrollingViewBehavior() {
    override fun onDependentViewChanged(
        parent: CoordinatorLayout, child: View,
        dependency: View
    ): Boolean {
        updateOffset(parent, child, dependency)
        return false
    }

    private fun updateOffset(
        parent: CoordinatorLayout, child: View,
        dependency: View
    ): Boolean {
        val behavior = (dependency
            .layoutParams as CoordinatorLayout.LayoutParams).behavior
        if (behavior is CoordinatorLayout.Behavior) {
            // Offset the child so that it is below the app-bar (with any
            // overlap)
            val offset = 0 // CHANGED TO 0
            topAndBottomOffset = offset
            return true
        }
        return false
    }
}