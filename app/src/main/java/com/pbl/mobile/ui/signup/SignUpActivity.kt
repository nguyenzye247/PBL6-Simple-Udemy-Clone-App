package com.pbl.mobile.ui.signup

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.core.view.isEmpty
import androidx.core.widget.addTextChangedListener
import com.pbl.mobile.R
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.ViewModelProviderFactory
import com.pbl.mobile.databinding.ActivitySignUpBinding
import com.pbl.mobile.extension.isEmailValid
import com.pbl.mobile.implement.TextChangeListner
import com.pbl.mobile.ui.signin.SignInActivity
import com.pbl.mobile.util.VersionChecker.isAndroid_M_AndAbove

class SignUpActivity : BaseActivity<ActivitySignUpBinding, SignUpViewModel>() {

    override fun getLazyBinding() = lazy { ActivitySignUpBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<SignUpViewModel> {
        ViewModelProviderFactory(BaseInput.NoInput)
    }

    override fun setupInit() {
        initClickEvent()
        initViews()
    }

    private fun initViews() {
        // init other views
    }

    private fun initClickEvent() {
        binding.btnCreateAccount.setOnClickListener {
            executeCreateAccountButtonClick()
        }
        binding.txtEmail.editText?.addTextChangedListener(object : TextChangeListner {
            override fun afterTextChanged(text: Editable?) {
                if (text.isEmailValid())
                    setValidEmailAppearance()
            }
        })
        binding.tvSignIn.setOnClickListener {
            goToSignIn()
        }
    }

    private fun executeCreateAccountButtonClick() {
        if (isAllInputNonEmpty()) {
            validateEmailInput()
        // Other account creation related actions
        } else
            warningEmptyInputs()
    }

    private fun isAllInputNonEmpty(): Boolean{
        return binding.txtFullName.isEmpty() &&
                binding.txtEmail.isEmpty() &&
                binding.txtPassword.isEmpty() &&
                binding.txtRepeatPassword.isEmpty()
    }

    private fun warningEmptyInputs() {
        binding.apply {

        }
    }

    private fun validateEmailInput() {
        val emailInputText = binding.txtEmail.editText?.text?.toString()
        if (emailInputText.isEmailValid()) {
            setValidEmailAppearance()
        } else
            setInvalidEmailAppearance()
    }

    private fun setValidEmailAppearance() {
        binding.txtEmail.isErrorEnabled = false
        binding.txtEmail.error = null
        if (isAndroid_M_AndAbove())
            binding.txtEmail.editText?.setTextColor(
                resources.getColor(
                    R.color.text_and_common_logo_color,
                    theme
                )
            )
        else
            binding.txtEmail.editText?.setTextColor(resources.getColor(R.color.text_and_common_logo_color))
    }

    private fun setInvalidEmailAppearance() {
        binding.txtEmail.isErrorEnabled = true
        binding.txtEmail.error = getString(R.string.error_invalid_email)
        if (isAndroid_M_AndAbove())
            binding.txtEmail.editText?.setTextColor(resources.getColor(R.color.red_error, theme))
        else
            binding.txtEmail.editText?.setTextColor(resources.getColor(R.color.red_error))
    }

    private fun goToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
