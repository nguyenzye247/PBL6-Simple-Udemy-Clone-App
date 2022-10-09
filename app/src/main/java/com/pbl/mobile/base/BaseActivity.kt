package com.pbl.mobile.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T: ViewBinding> : AppCompatActivity() {
    protected val binding by getLazyBinding()

    abstract fun getLazyBinding(): Lazy<T>
    abstract fun setupInit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupInit()
    }

}