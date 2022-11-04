package com.pbl.mobile.ui.new_password

import androidx.activity.viewModels
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.ViewModelProviderFactory
import com.pbl.mobile.databinding.ActivitySetNewPasswordBinding
import com.pbl.mobile.ui.forgot_password.ForgotPasswordViewModel

class NewPasswordActivity : BaseActivity<ActivitySetNewPasswordBinding, NewPasswordViewModel>() {
    override fun getLazyBinding() = lazy { ActivitySetNewPasswordBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<NewPasswordViewModel> {
        ViewModelProviderFactory(BaseInput.NoInput)
    }

    override fun setupInit() {

    }
}
