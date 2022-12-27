package com.pbl.mobile.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pbl.mobile.ui.course.CourseDetailsViewModel
import com.pbl.mobile.ui.editprofile.EditProfileViewModel
import com.pbl.mobile.ui.forgot_password.ForgotPasswordViewModel
import com.pbl.mobile.ui.main.HomeMainViewModel
import com.pbl.mobile.ui.new_password.NewPasswordViewModel
import com.pbl.mobile.ui.purchase.PurchaseViewModel
import com.pbl.mobile.ui.search.SearchViewModel
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
            modelClass.isAssignableFrom(CourseDetailsViewModel::class.java) -> {
                return CourseDetailsViewModel(input as BaseInput.CourseDetailInput) as T
            }
            modelClass.isAssignableFrom(WatchLectureViewModel::class.java) -> {
                return WatchLectureViewModel(input as BaseInput.WatchLectureInput) as T
            }
            modelClass.isAssignableFrom(PurchaseViewModel::class.java) -> {
                return PurchaseViewModel(input as BaseInput.PurchaseInput) as T
            }
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                return EditProfileViewModel(input as BaseInput.EditProFileInput) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                return SearchViewModel(input as BaseInput.SearchInput) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
