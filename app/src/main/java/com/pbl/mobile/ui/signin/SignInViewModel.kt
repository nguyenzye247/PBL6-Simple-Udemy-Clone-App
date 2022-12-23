package com.pbl.mobile.ui.signin

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onesignal.OneSignal
import com.pbl.mobile.R
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.api.SUCCESS
import com.pbl.mobile.api.SessionManager
import com.pbl.mobile.api.signin.SignInRequestManager
import com.pbl.mobile.api.user.UserRequestManager
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.BaseViewModel
import com.pbl.mobile.common.EMPTY_TEXT
import com.pbl.mobile.extension.getBaseConfig
import com.pbl.mobile.extension.observeOnUiThread
import com.pbl.mobile.extension.showToast
import com.pbl.mobile.model.remote.signin.SignInRequest
import com.pbl.mobile.model.remote.signin.SignInResponse
import com.pbl.mobile.ui.main.HomeMainActivity
import com.pbl.mobile.util.NetworkUtil
import io.sentry.Sentry
import okhttp3.ResponseBody
import retrofit2.HttpException

class SignInViewModel(input: BaseInput.MainInput) : BaseViewModel(input) {
    private val pApplication = input.application
    private val signInRequestManager = SignInRequestManager()
    private val userRequestManager = UserRequestManager()

    private val _loginResult: MutableLiveData<BaseResponse<SignInResponse>> = MutableLiveData()

    fun loginResult(): LiveData<BaseResponse<SignInResponse>> = _loginResult

    fun login(email: String, password: String) {
        _loginResult.value = BaseResponse.Loading()
        try {
            addDisposables(
                signInRequestManager.login(pApplication, SignInRequest(email, password))
                    .observeOnUiThread()
                    .doOnSubscribe {
                        _loginResult.value = BaseResponse.Loading()
                    }
                    .subscribe(
                        { response ->
                            handleResponse(response)
                        },
                        { throwable ->
                            if (NetworkUtil.isHttpStatusCode(throwable, 400)) {
                                val body: ResponseBody? =
                                    (throwable as HttpException).response()?.errorBody()
                                body?.let {
                                    if (it.string().contains("confirmed"))
                                        _loginResult.value =
                                            BaseResponse.Error("User not confirmed")
                                    else
                                        handelError(throwable)
                                }
                            } else {
                                handelError(throwable)
                            }
                        }
                    )
            )
        } catch (ex: Exception) {
            _loginResult.value = BaseResponse.Error(ex.message)
        }
    }

    fun getMe() {
        subscription.add(
            userRequestManager.getMe(
                pApplication,
                pApplication.getBaseConfig().token
            )
                .observeOnUiThread()
                .subscribe(
                    { getMeResponse ->
                        getMeResponse.data.let { me ->
                            OneSignal.setExternalUserId(me.userId)
                            pApplication.getBaseConfig().apply {
                                val idd = me.userId
                                myId = me.userId
                                myAvatar = me.avatarUrl ?: EMPTY_TEXT
                                myRole = me.role
                            }
                        }
                    },
                    { throwable ->

                    }
                )
        )
    }

    fun progressLogin(result: SignInResponse) {
        pApplication.let {
            pApplication.showToast(it.getString(R.string.sign_in_success))
            SessionManager.saveToken(it, result.data.token)
            SessionManager.saveRefreshToken(it, result.data.refreshToken)
            SessionManager.saveExpiresTime(it, result.data.expiresIn)
            navigateToHome()
        }
    }

    fun navigateToHome() {
        pApplication.let {
            val intent = Intent(it, HomeMainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.startActivity(intent)
        }
    }

    private fun handleResponse(response: SignInResponse) {
        if (response.status == SUCCESS)
            _loginResult.value = BaseResponse.Success(response)
        else
            _loginResult.value = BaseResponse.Error("Incorrect email or password")
    }

    private fun handelError(throwable: Throwable) {
        Sentry.captureException(throwable)
        _loginResult.value = BaseResponse.Error("Incorrect email or password")
    }
}
