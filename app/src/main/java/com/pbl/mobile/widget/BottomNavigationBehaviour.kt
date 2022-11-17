package com.pbl.mobile.widget
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationBehaviour: CoordinatorLayout.Behavior<BottomNavigationView>() {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: BottomNavigationView,
        dependency: View
    ): Boolean {
        return super.layoutDependsOn(parent, child, dependency)
    }
}