package com.pbl.mobile.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pbl.mobile.ui.course.CourseViewModel
import com.pbl.mobile.ui.forgot_password.ForgotPasswordViewModel
import com.pbl.mobile.ui.main.HomeMainViewModel
import com.pbl.mobile.ui.new_password.NewPasswordViewModel
import com.pbl.mobile.ui.signin.SignInViewModel
import com.pbl.mobile.ui.signup.SignUpViewModel
import com.pbl.mobile.ui.verify_email.VerifyEmailViewModel
import com.pbl.mobile.ui.watchlecture.WatchLectureViewModel

class ViewModelProviderFactory(private val input: BaseInput) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(HomeMainViewModel::class.java) -> {
                return HomeMainViewModel(input as BaseInput.MainInput) as T
            }
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                return SignInViewModel(input as BaseInput.MainInput) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                return SignUpViewModel(input as BaseInput.MainInput) as T
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
            modelClass.isAssignableFrom(CourseViewModel::class.java) -> {
                return CourseViewModel(input as BaseInput.CourseDetailInput) as T
            }
            modelClass.isAssignableFrom(WatchLectureViewModel::class.java) -> {
                return WatchLectureViewModel(input as BaseInput.WatchLectureInput) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
