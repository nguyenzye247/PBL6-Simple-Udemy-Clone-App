package com.pbl.mobile.ui.signup

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pbl.mobile.R
import com.pbl.mobile.api.BEARER
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.api.SUCCESS
import com.pbl.mobile.api.SessionManager
import com.pbl.mobile.api.signup.SignUpRequestManager
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.BaseViewModel
import com.pbl.mobile.extension.getBaseConfig
import com.pbl.mobile.extension.observeOnUiThread
import com.pbl.mobile.extension.showToast
import com.pbl.mobile.model.remote.signup.SignUpRequest
import com.pbl.mobile.model.remote.signup.SignUpResponse
import com.pbl.mobile.ui.signin.SignInActivity

class SignUpViewModel(input: BaseInput.MainInput) : BaseViewModel(input) {
    private val pApplication = input.application
    private val signUpRequestManager = SignUpRequestManager()
    private val token = BEARER + SessionManager.fetchToken(pApplication)

    private val _registerResult: MutableLiveData<BaseResponse<SignUpResponse>> = MutableLiveData()

    fun registerResult(): LiveData<BaseResponse<SignUpResponse>> = _registerResult

    fun register(email: String, password: String, fullName: String) {
        _registerResult.value = BaseResponse.Loading()
        try {
            addDisposables(
                signUpRequestManager.register(pApplication, token, SignUpRequest(email, password, fullName))
                    .observeOnUiThread()
                    .doOnSubscribe {
                        _registerResult.value = BaseResponse.Loading()
                    }
                    .subscribe(
                        { response ->
                            handleResponse(response)
                        },
                        { throwable ->
                            handelError(throwable)
                        }
                    )
            )
        } catch (ex: Exception) {
            _registerResult.value = BaseResponse.Error(ex.message)
        }
    }

    fun progressRegister(result: SignUpResponse) {
        pApplication.let {
            pApplication.showToast(it.getString(R.string.sign_up_success))
            it.getBaseConfig().isActivated = result.data.isActivated
            it.getBaseConfig().role = result.data.role
            navigateToSignIn()
        }
    }

    private fun navigateToSignIn() {
        pApplication.let {
            val intent = Intent(it, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.startActivity(intent)
        }
    }

    private fun handleResponse(response: SignUpResponse) {
        if (response.status == SUCCESS)
            _registerResult.value = BaseResponse.Success(response)
        else
            _registerResult.value = BaseResponse.Error("FAILED")
    }

    private fun handelError(throwable: Throwable) {
        _registerResult.value = BaseResponse.Error(throwable.message)
    }

}