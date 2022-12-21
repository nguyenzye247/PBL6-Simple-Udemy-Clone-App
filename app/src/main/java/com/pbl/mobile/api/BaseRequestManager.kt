package com.pbl.mobile.api

import android.app.Application
import com.pbl.mobile.util.TokenAuthenticator
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
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

    private val headerInterceptor = Interceptor { chain ->
        val original: Request = chain.request()
        val request: Request =
            original.newBuilder()
                .header("Authorization", BEARER + SessionManager.fetchToken(application))
                .method(original.method, original.body)
                .build()
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient
        .Builder()
        .authenticator(TokenAuthenticator(application))
        .addInterceptor(headerInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    val myRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
