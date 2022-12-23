package com.pbl.mobile.api.payment

import android.app.Application
import com.pbl.mobile.api.BaseRequestManager
import com.pbl.mobile.api.GET_PAYMENT_URL
import com.pbl.mobile.api.PAYMENT_URL
import com.pbl.mobile.api.POST_PAYMENT_URL
import com.pbl.mobile.model.remote.payment.GetPaymentResponse
import com.pbl.mobile.model.remote.payment.post.PostPaymentBody
import com.pbl.mobile.model.remote.payment.post.PostPaymentResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentApi {
    companion object {
        fun getApi(application: Application): PaymentApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(PaymentApi::class.java)
        }
    }

    @GET("$GET_PAYMENT_URL/{userId}/$PAYMENT_URL")
    fun getPayments(@Path("userId") userId: String): Single<GetPaymentResponse>

    @POST(POST_PAYMENT_URL)
    fun postPayments(@Body paymentBody: PostPaymentBody): Single<PostPaymentResponse>
}
