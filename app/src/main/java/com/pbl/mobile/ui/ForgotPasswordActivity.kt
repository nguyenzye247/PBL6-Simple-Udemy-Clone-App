package com.pbl.mobile.ui

import android.content.Intent
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.databinding.ActivityForgotPasswordBinding
import com.pbl.mobile.ui.signin.SignInActivity

class ForgotPasswordActivity : BaseActivity<ActivityForgotPasswordBinding>() {

    override fun getLazyBinding() = lazy { ActivityForgotPasswordBinding.inflate(layoutInflater) }

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