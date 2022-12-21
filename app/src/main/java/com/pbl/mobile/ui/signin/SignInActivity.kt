package com.pbl.mobile.ui.signin

import android.content.Intent
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.pbl.mobile.R
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.api.SessionManager
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.ViewModelProviderFactory
import com.pbl.mobile.common.EMPTY_SPACE
import com.pbl.mobile.databinding.ActivitySignInBinding
import com.pbl.mobile.extension.isEmailValid
import com.pbl.mobile.extension.setError
import com.pbl.mobile.extension.showToast
import com.pbl.mobile.ui.forgot_password.ForgotPasswordActivity
import com.pbl.mobile.ui.signup.SignUpActivity
import com.pbl.mobile.util.VersionChecker

class SignInActivity : BaseActivity<ActivitySignInBinding, SignInViewModel>() {

    override fun getLazyBinding() = lazy { ActivitySignInBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<SignInViewModel> {
        ViewModelProviderFactory(BaseInput.MainInput(application))
    }

    override fun setupInit() {
        loginIfExisted()
        initView()
        initClickEvent()
        observe()
    }

    private fun loginIfExisted() {
        val expiresTime = SessionManager.fetchExpiresTime(this@SignInActivity)
        if (System.currentTimeMillis() <= expiresTime) {
            viewModel.getMe()
            viewModel.navigateToHome()
        }
    }

    private fun initView() {
        // init other views
    }

    private fun initClickEvent() {
        binding.btnSignIn.setOnClickListener {
            executeSignInButtonClick()
        }
        binding.tvForgotPassword.setOnClickListener {
            forgotPassword()
        }
        binding.tvSignUp.setOnClickListener {
            goToSignUp()
        }
    }

    private fun observe() {
        viewModel.loginResult().observe(this) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    showLoading()
                }
                is BaseResponse.Success -> {
                    stopLoading()
                    setExistedAccountAppearance()
                    viewModel.getMe()
                    response.data?.let { viewModel.progressLogin(it) }
                }
                is BaseResponse.Error -> {
                    response.msg?.let { this@SignInActivity.showToast(it) }
                    stopLoading()
                    setNonExistedAccountAppearance()
                    showError()
                }
            }
        }
    }

    private fun signIn() {
        val emailInputText = binding.txtEmail.editText?.text.toString()
        val passwordInputText = binding.txtPassword.editText?.text.toString()
        viewModel.login(emailInputText, passwordInputText)
    }

    private fun executeSignInButtonClick() {
        if (validateEmailInput())
            signIn()
    }

    private fun validateEmailInput(): Boolean {
        val emailInputText = binding.txtEmail.editText?.text?.toString()
        if (emailInputText.isEmailValid()) {
            setValidEmailAppearance()
            return true
        } else
            setInvalidEmailAppearance()
        return false
    }

    private fun setValidEmailAppearance() {
        binding.txtEmail.setError(false, null)
        if (VersionChecker.isAndroid_M_AndAbove())
            binding.txtEmail.editText?.setTextColor(
                resources.getColor(
                    R.color.black_100,
                    theme
                )
            )
        else
            binding.txtEmail.editText?.setTextColor(resources.getColor(R.color.black_100))
    }

    private fun setInvalidEmailAppearance() {
        binding.txtEmail.setError(true, getString(R.string.error_invalid_email))
        if (VersionChecker.isAndroid_M_AndAbove())
            binding.txtEmail.editText?.setTextColor(resources.getColor(R.color.red_100, theme))
        else
            binding.txtEmail.editText?.setTextColor(resources.getColor(R.color.red_100))
    }

    private fun setExistedAccountAppearance() {
        binding.txtEmail.isErrorEnabled = false
        binding.txtEmail.error = null
        binding.txtPassword.isErrorEnabled = false
        binding.txtPassword.error = null

        if (VersionChecker.isAndroid_M_AndAbove()) {
            binding.txtEmail.editText?.setTextColor(
                resources.getColor(
                    R.color.black_100,
                    theme
                )
            )
            binding.txtPassword.editText?.setTextColor(
                resources.getColor(
                    R.color.black_100,
                    theme
                )
            )
        } else {
            binding.txtEmail.editText?.setTextColor(resources.getColor(R.color.black_100))
            binding.txtPassword.editText?.setTextColor(resources.getColor(R.color.black_100))
        }
    }

    private fun setNonExistedAccountAppearance() {
        binding.txtEmail.isErrorEnabled = true
        binding.txtEmail.error = EMPTY_SPACE
        binding.txtPassword.isErrorEnabled = true
        binding.txtPassword.error = getString(R.string.email_or_password_incorrect)

        if (VersionChecker.isAndroid_M_AndAbove()) {
            binding.txtEmail.editText?.setTextColor(resources.getColor(R.color.red_100, theme))
            binding.txtPassword.editText?.setTextColor(resources.getColor(R.color.red_100, theme))
        } else {
            binding.txtEmail.editText?.setTextColor(resources.getColor(R.color.red_100))
            binding.txtPassword.editText?.setTextColor(resources.getColor(R.color.red_100))
        }
    }

    private fun forgotPassword() {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun goToSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun showLoading() {
        binding.progressSignIn.isVisible = true
    }

    private fun stopLoading() {
        binding.progressSignIn.isVisible = false
    }

    private fun showError() {
        //TODO: show login fail custom toast
    }
}