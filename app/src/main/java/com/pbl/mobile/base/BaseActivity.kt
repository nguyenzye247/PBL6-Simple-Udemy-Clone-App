package com.pbl.mobile.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseActivity<T : ViewBinding, V : BaseViewModel> : AppCompatActivity() {
    protected val subscription: CompositeDisposable = CompositeDisposable()
    abstract fun getLazyBinding(): Lazy<T>
    abstract fun getLazyViewModel(): Lazy<V>
    abstract fun setupInit()

    protected val binding by getLazyBinding()
    protected val viewModel by getLazyViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupInit()
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription.clear()
    }

    fun makeStatusBarLight() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    fun makeStatusBarTransparent() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
    }
}