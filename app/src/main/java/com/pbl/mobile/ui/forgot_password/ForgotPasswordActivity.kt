package com.pbl.mobile.ui.forgot_password

import android.content.Intent
import androidx.activity.viewModels
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.ViewModelProviderFactory
import com.pbl.mobile.databinding.ActivityForgotPasswordBinding
import com.pbl.mobile.ui.signin.SignInActivity
import com.pbl.mobile.ui.verify_email.VerifyEmailActivity

class ForgotPasswordActivity : BaseActivity<ActivityForgotPasswordBinding, ForgotPasswordViewModel>() {

    override fun getLazyBinding() = lazy { ActivityForgotPasswordBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<ForgotPasswordViewModel> {
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
        binding.btnSend.setOnClickListener{
            verifyEmail(binding.txtEmail.editText?.text.toString())
        }
        binding.tvSignIn.setOnClickListener {
            goToSignIn()
        }
    }

    private fun verifyEmail(emailText : String){
        sendToEmail(emailText)
        goToVerifyEmail()
    }

    private fun sendToEmail(emailText : String){

    }

    private fun goToVerifyEmail(){
        val intent = Intent(this, VerifyEmailActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun goToSignIn(){
        val intent = Intent(this, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}