package com.pbl.mobile.api.image

import android.app.Application
import com.pbl.mobile.model.remote.image.UploadImageResponse
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.http.Multipart

class ImageRequestManager {
    fun uploadFrontImage(application: Application, frontImage: MultipartBody.Part): Single<UploadImageResponse> {
        return ImageUploadApi.getApi(application).uploadFrontImage(frontImage)
    }

    fun uploadBackImage(application: Application, backImage: MultipartBody.Part): Single<UploadImageResponse> {
        return ImageUploadApi.getApi(application).uploadBackImage(backImage)
    }

    fun uploadAvatar(application: Application, avatar: MultipartBody.Part): Single<UploadImageResponse> {
        return ImageUploadApi.getApi(application).uploadAvatar(avatar)
    }
}
