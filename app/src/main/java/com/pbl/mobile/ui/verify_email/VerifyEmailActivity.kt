package com.pbl.mobile.ui.verify_email

import androidx.activity.viewModels
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.ViewModelProviderFactory
import com.pbl.mobile.databinding.ActivityVerifyEmailBinding
import com.pbl.mobile.ui.signup.SignUpViewModel

class VerifyEmailActivity : BaseActivity<ActivityVerifyEmailBinding, VerifyEmailViewModel>() {

    override fun getLazyBinding() = lazy { ActivityVerifyEmailBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<VerifyEmailViewModel> {
        ViewModelProviderFactory(BaseInput.NoInput)
    }

    override fun setupInit() {

    }
}