package com.pbl.mobile.ui.purchase

import android.content.ComponentName
import android.net.Uri
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.ViewModelProviderFactory
import com.pbl.mobile.common.EMPTY_TEXT
import com.pbl.mobile.common.PURCHASE_URL_KEY
import com.pbl.mobile.databinding.ActivityPurchaseBinding


class PurchaseActivity : BaseActivity<ActivityPurchaseBinding, PurchaseViewModel>() {
    private var mCustomTabsClient: CustomTabsClient? = null

    companion object {
        private const val CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome"
    }

    override fun getLazyBinding() = lazy { ActivityPurchaseBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<PurchaseViewModel> {
        ViewModelProviderFactory(
            BaseInput.PurchaseInput(
                application,
                intent.getStringExtra(PURCHASE_URL_KEY) ?: EMPTY_TEXT
            )
        )
    }


    override fun setupInit() {
        initViews()
        initListeners()
        observe()
    }

    private fun initViews() {
        val paymentUrl = intent.getStringExtra(PURCHASE_URL_KEY) ?: EMPTY_TEXT
        binding.apply {

        }
    }

    private fun initListeners() {

    }

    private fun observe() {

    }

}
