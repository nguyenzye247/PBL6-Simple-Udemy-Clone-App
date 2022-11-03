package com.pbl.mobile.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pbl.mobile.ui.home.HomeViewModel

class ViewModelProviderFactory(private val input: BaseInput) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel(input as BaseInput.MainInput) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}