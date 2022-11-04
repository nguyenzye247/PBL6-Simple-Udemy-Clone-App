package com.pbl.mobile.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pbl.mobile.ui.forgot_password.ForgotPasswordViewModel
import com.pbl.mobile.ui.home.HomeViewModel
import com.pbl.mobile.ui.new_password.NewPasswordViewModel
import com.pbl.mobile.ui.signin.SignInViewModel
import com.pbl.mobile.ui.signup.SignUpViewModel
import com.pbl.mobile.ui.verify_email.VerifyEmailViewModel

class ViewModelProviderFactory(private val input: BaseInput) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel(input as BaseInput.MainInput) as T
            }
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                return SignInViewModel(input as BaseInput.MainInput) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                return SignUpViewModel(input as BaseInput.NoInput) as T
            }
            modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java) -> {
                return ForgotPasswordViewModel(input as BaseInput.NoInput) as T
            }
            modelClass.isAssignableFrom(NewPasswordViewModel::class.java) -> {
                return NewPasswordViewModel(input as BaseInput.NoInput) as T
            }
            modelClass.isAssignableFrom(VerifyEmailViewModel::class.java) -> {
                return VerifyEmailViewModel(input as BaseInput.NoInput) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
