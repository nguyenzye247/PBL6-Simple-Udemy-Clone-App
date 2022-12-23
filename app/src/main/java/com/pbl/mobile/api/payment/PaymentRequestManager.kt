package com.pbl.mobile.api.payment

import android.app.Application
import com.pbl.mobile.model.remote.payment.GetPaymentResponse
import com.pbl.mobile.model.remote.payment.post.PostPaymentBody
import com.pbl.mobile.model.remote.payment.post.PostPaymentResponse
import io.reactivex.rxjava3.core.Single

class PaymentRequestManager {
    fun getPayment(application: Application, userId: String): Single<GetPaymentResponse> {
        return PaymentApi.getApi(application).getPayments(userId)
    }

    fun postPayment(
        application: Application,
        paymentBody: PostPaymentBody
    ): Single<PostPaymentResponse> {
        return PaymentApi.getApi(application).postPayments(paymentBody)
    }
}
