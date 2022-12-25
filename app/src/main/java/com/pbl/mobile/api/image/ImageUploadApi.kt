package com.pbl.mobile.api.image

import android.app.Application
import com.pbl.mobile.api.BaseRequestManager
import com.pbl.mobile.api.UPDATE_AVATAR_URL
import com.pbl.mobile.api.UPLOAD_IMAGE_URL
import com.pbl.mobile.model.remote.image.UploadImageResponse
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ImageUploadApi {
    companion object {
        fun getApi(application: Application): ImageUploadApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(ImageUploadApi::class.java)
        }
    }

    @Multipart
    @POST(UPLOAD_IMAGE_URL)
    fun uploadFrontImage(
        @Part file: MultipartBody.Part
    ): Single<UploadImageResponse>

    @Multipart
    @POST(UPLOAD_IMAGE_URL)
    fun uploadBackImage(
        @Part file: MultipartBody.Part
    ): Single<UploadImageResponse>

    @Multipart
    @PUT(UPLOAD_IMAGE_URL)
    fun uploadAvatar(
        @Part file: MultipartBody.Part
    ): Single<UploadImageResponse>
}
