package com.pbl.mobile.ui.forgot_password

import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.databinding.ActivitySetNewPasswordBinding

class NewPasswordActivity : BaseActivity<ActivitySetNewPasswordBinding>() {
    override fun getLazyBinding() = lazy { ActivitySetNewPasswordBinding.inflate(layoutInflater) }

    override fun setupInit() {

    }
}
