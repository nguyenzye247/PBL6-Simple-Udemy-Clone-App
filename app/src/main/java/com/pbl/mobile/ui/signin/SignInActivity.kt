package com.pbl.mobile.ui.signin

import android.content.Intent
import com.pbl.mobile.R
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.common.EMPTY_SPACE
import com.pbl.mobile.databinding.ActivitySignInBinding
import com.pbl.mobile.ui.forgot_password.ForgotPasswordActivity
import com.pbl.mobile.ui.signup.SignUpActivity
import com.pbl.mobile.util.VersionChecker

class SignInActivity : BaseActivity<ActivitySignInBinding>() {

    override fun getLazyBinding() = lazy { ActivitySignInBinding.inflate(layoutInflater) }

    override fun setupInit() {
        initView()
        initClickEvent()
    }

    private fun initView(){
        // init other views
    }

    private fun initClickEvent(){
        binding.btnSignIn.setOnClickListener{
            signIn()
        }
        binding.tvForgotPassword.setOnClickListener {
            forgotPassword()
        }
        binding.tvSignUp.setOnClickListener {
            goToSignUp()
        }
    }

    private fun signIn(){
        val emailInputText = binding.txtEmail.editText?.text.toString()
        val passwordInputText = binding.txtPassword.editText?.text.toString()

        if (isAccountExisted(emailInputText, passwordInputText))
            setExistedAccountAppearance()
        else
            setNonExistedAccountAppearance()
    }

    private fun isAccountExisted(emailInputText : String, passwordInputText : String) : Boolean{
        return emailInputText == testEmail && passwordInputText == testPassword
    }

    private fun setExistedAccountAppearance(){
        binding.txtEmail.isErrorEnabled = false
        binding.txtEmail.error = null
        binding.txtPassword.isErrorEnabled = false
        binding.txtPassword.error = null

        if (VersionChecker.isAndroid_M_AndAbove()) {
            binding.txtEmail.editText?.setTextColor(
                resources.getColor(
                    R.color.text_and_common_logo_color,
                    theme
                )
            )
            binding.txtPassword.editText?.setTextColor(
                resources.getColor(
                    R.color.text_and_common_logo_color,
                    theme
                )
            )
        }
        else {
            binding.txtEmail.editText?.setTextColor(resources.getColor(R.color.text_and_common_logo_color))
            binding.txtPassword.editText?.setTextColor(resources.getColor(R.color.text_and_common_logo_color))
        }
    }

    private fun setNonExistedAccountAppearance(){
        binding.txtEmail.isErrorEnabled = true
        binding.txtEmail.error = EMPTY_SPACE
        binding.txtPassword.isErrorEnabled = true
        binding.txtPassword.error = getString(R.string.email_or_password_incorrect)

        if (VersionChecker.isAndroid_M_AndAbove()) {
            binding.txtEmail.editText?.setTextColor(resources.getColor(R.color.red_error, theme))
            binding.txtPassword.editText?.setTextColor(resources.getColor(R.color.red_error, theme))
        }
        else {
            binding.txtEmail.editText?.setTextColor(resources.getColor(R.color.red_error))
            binding.txtPassword.editText?.setTextColor(resources.getColor(R.color.red_error))
        }
    }

    private fun forgotPassword(){
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun goToSignUp(){
        val intent = Intent(this, SignUpActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    companion object{
        const val testEmail = "nguyen@gmail.com"
        const val testPassword = "123123"
    }

}