package com.pbl.mobile.ui.signup

import android.content.Intent
import android.text.Editable
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.pbl.mobile.R
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.ViewModelProviderFactory
import com.pbl.mobile.databinding.ActivitySignUpBinding
import com.pbl.mobile.extension.isEmailValid
import com.pbl.mobile.extension.setError
import com.pbl.mobile.implement.TextChangeListner
import com.pbl.mobile.ui.signin.SignInActivity
import com.pbl.mobile.util.VersionChecker.isAndroid_M_AndAbove

class SignUpActivity : BaseActivity<ActivitySignUpBinding, SignUpViewModel>() {

    override fun getLazyBinding() = lazy { ActivitySignUpBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<SignUpViewModel> {
        ViewModelProviderFactory(BaseInput.MainInput(application))
    }

    override fun setupInit() {
        initListener()
        initViews()
        observe()
    }

    private fun initViews() {
        // init other views
    }

    private fun initListener() {
        binding.apply {
            btnCreateAccount.setOnClickListener {
                executeCreateAccountButtonClick()
            }
            txtEmail.editText?.addTextChangedListener(object : TextChangeListner {
                override fun afterTextChanged(text: Editable?) {
                    if (text?.isNotEmpty() == true) {
                        binding.txtEmail.setError(false, null)
                        if (text.isEmailValid())
                            setValidEmailAppearance()
                    }
                }
            })
            txtFullName.editText?.addTextChangedListener(object : TextChangeListner {
                override fun afterTextChanged(text: Editable?) {
                    if (text?.isNotEmpty() == true)
                        binding.txtFullName.setError(false, null)
                }
            })
            txtPassword.editText?.addTextChangedListener(object : TextChangeListner {
                override fun afterTextChanged(text: Editable?) {
                    if (text?.isNotEmpty() == true) {
                        binding.txtPassword.setError(false, null)
                        if (validatePasswordInput())
                            binding.txtPassword.setError(false, null)
                    }
                }
            })
            txtRepeatPassword.editText?.addTextChangedListener(object : TextChangeListner {
                override fun afterTextChanged(text: Editable?) {
                    if (text?.isNotEmpty() == true) {
                        binding.txtRepeatPassword.setError(false, null)
                        if (validatePasswordInput())
                            binding.txtPassword.setError(false, null)
                    }
                }
            })
            tvSignIn.setOnClickListener {
                goToSignIn()
            }
        }
    }

    private fun observe() {
        viewModel.registerResult().observe(this) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    showLoading()
                }
                is BaseResponse.Success -> {
                    stopLoading()
                    response.data?.let { viewModel.progressRegister(it) }
                }
                is BaseResponse.Error -> {
                    stopLoading()
                    showError()
                }
                else -> {}
            }
        }
    }

    private fun executeCreateAccountButtonClick() {
        if (!isAllInputEmpty()) {
            if (validateEmailInput() && validatePasswordInput())
                signUp()
        } else
            warningEmptyInputs()
    }

    private fun signUp() {
        val emailInputText = binding.txtEmail.editText?.text.toString()
        val passwordInputText = binding.txtPassword.editText?.text.toString()
        val fullNameInputText = binding.txtFullName.editText?.text.toString()
        viewModel.register(emailInputText, passwordInputText, fullNameInputText)
    }

    private fun isAllInputEmpty(): Boolean {
        return binding.txtFullName.editText?.text?.isEmpty() == true &&
                binding.txtEmail.editText?.text?.isEmpty() == true &&
                binding.txtPassword.editText?.text?.isEmpty() == true &&
                binding.txtRepeatPassword.editText?.text?.isEmpty() == true
    }

    private fun warningEmptyInputs() {
        binding.apply {
            txtFullName.setError(true, getString(R.string.field_cant_not_be_empty))
            txtEmail.setError(true, getString(R.string.field_cant_not_be_empty))
            txtPassword.setError(true, getString(R.string.field_cant_not_be_empty))
            txtRepeatPassword.setError(true, getString(R.string.field_cant_not_be_empty))
        }
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

    private fun validatePasswordInput(): Boolean {
        val passwordInputText = binding.txtPassword.editText?.text?.toString()
        val repeatPasswordInputText = binding.txtRepeatPassword.editText?.text?.toString()
        if (passwordInputText != null && repeatPasswordInputText != null) {
            if (passwordInputText != repeatPasswordInputText) {
                binding.txtPassword.setError(true, getString(R.string.confirm_password_error))
                binding.txtRepeatPassword.setError(true, getString(R.string.confirm_password_error))
                return false
            }
        }
        return true
    }

    private fun setValidEmailAppearance() {
        binding.txtEmail.setError(false, null)
        if (isAndroid_M_AndAbove())
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
        if (isAndroid_M_AndAbove())
            binding.txtEmail.editText?.setTextColor(resources.getColor(R.color.red_100, theme))
        else
            binding.txtEmail.editText?.setTextColor(resources.getColor(R.color.red_100))
    }

    private fun goToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun showLoading() {
        binding.progressSignUp.isVisible = true
    }

    private fun stopLoading() {
        binding.progressSignUp.isVisible = false
    }

    private fun showError() {
        //TODO: show register fail custom toast
    }

    private fun resetInputFields() {
        binding.apply {
            txtFullName.setError(false, null)
            txtEmail.setError(false, null)
            txtPassword.setError(false, null)
            txtRepeatPassword.setError(false, null)
        }
    }
}
