package com.pbl.mobile.ui.signin

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pbl.mobile.R
import com.pbl.mobile.api.BEARER
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.api.SessionManager
import com.pbl.mobile.api.signin.SignInRequestManager
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.BaseViewModel
import com.pbl.mobile.extension.observeOnUiThread
import com.pbl.mobile.extension.showToast
import com.pbl.mobile.model.remote.signin.SignInRequest
import com.pbl.mobile.model.remote.signin.SignInResponse
import com.pbl.mobile.ui.home.HomeActivity

class SignInViewModel(input: BaseInput.MainInput) : BaseViewModel(input) {
    private val pApplication = input.application
    private val signInRequestManager = SignInRequestManager()
    private val token = BEARER+SessionManager.fetchToken(pApplication)

    private val _loginResult: MutableLiveData<BaseResponse<SignInResponse>> = MutableLiveData()

    fun loginResult(): LiveData<BaseResponse<SignInResponse>> = _loginResult

    fun login(email: String, password: String) {
        _loginResult.value = BaseResponse.Loading()
        try {
            addDisposables(
                signInRequestManager.login(pApplication, token, SignInRequest(email, password))
                    .observeOnUiThread()
                    .doOnSubscribe {
                        _loginResult.value = BaseResponse.Loading()
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
            _loginResult.value = BaseResponse.Error(ex.message)
        }

    }

    fun progressLogin(result: SignInResponse) {
        pApplication?.let {
            pApplication.showToast(it.getString(R.string.sign_in_success))
            SessionManager.saveToken(it, result.data.token)
            SessionManager.saveRefreshToken(it, result.data.refreshToken)
            navigateToHome()
        }
    }

    fun navigateToHome() {
        pApplication?.let {
            val intent = Intent(it, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.startActivity(intent)
        }
    }

    private fun handleResponse(response: SignInResponse) {
        if (response.status == "success")
            _loginResult.value = BaseResponse.Success(response)
        else
            _loginResult.value = BaseResponse.Error("FAILED")
    }

    private fun handelError(throwable: Throwable) {
        _loginResult.value = BaseResponse.Error(throwable.message)
    }
}
