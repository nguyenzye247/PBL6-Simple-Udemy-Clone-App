package com.pbl.mobile.api

import android.app.Application
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BaseRequestManager(val application: Application) {

    companion object {
        @Volatile
        private var INSTANCE: BaseRequestManager? = null

        fun getInstance(application: Application): BaseRequestManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BaseRequestManager(application).also { INSTANCE = it }
            }
    }

    private var httpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val dispatcher = Dispatcher().also {
        it.maxRequests = 1
    }

    private val okHttpClient = OkHttpClient
        .Builder()
        .dispatcher(dispatcher)
        .addInterceptor(AuthenticationInterceptor(application))
        .addInterceptor(httpLoggingInterceptor)
        .build()

    val myRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
