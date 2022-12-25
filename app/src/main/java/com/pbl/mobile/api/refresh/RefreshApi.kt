package com.pbl.mobile.api.refresh

import android.app.Application
import com.google.gson.annotations.SerializedName
import com.pbl.mobile.api.BaseRequestManager
import com.pbl.mobile.model.remote.signin.SignInResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.Body

interface RefreshApi {
    companion object {
        fun getApi(application: Application): RefreshApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(RefreshApi::class.java)
        }
    }

    fun refreshToken(@Body refreshBody: RefreshBody): Call<SignInResponse>

    data class RefreshBody(
        @SerializedName("refreshToken")
        val refreshToken: String
    )
}
