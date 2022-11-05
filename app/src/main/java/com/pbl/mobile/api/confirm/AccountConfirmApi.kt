package com.pbl.mobile.api.confirm

import android.app.Application
import com.pbl.mobile.api.BaseRequestManager
import com.pbl.mobile.api.CONFIRM_EMAIL_URL
import com.pbl.mobile.model.remote.confirm.ConfirmResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AccountConfirmApi {
    companion object {
        fun getApi(application: Application): AccountConfirmApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(AccountConfirmApi::class.java)
        }
    }

    @GET("$CONFIRM_EMAIL_URL/{confirmCode}")
    fun confirm(@Path("confirmCode") confirmCode: String): ConfirmResponse
}
