package com.pbl.mobile.ui

import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.databinding.ActivityVerifyEmailBinding

class VerifyEmailActivity : BaseActivity<ActivityVerifyEmailBinding>() {

    override fun getLazyBinding() = lazy { ActivityVerifyEmailBinding.inflate(layoutInflater) }

    override fun setupInit() {

    }
}