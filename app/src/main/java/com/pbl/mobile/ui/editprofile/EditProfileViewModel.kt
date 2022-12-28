package com.pbl.mobile.ui.editprofile

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.api.image.ImageRequestManager
import com.pbl.mobile.api.user.UserRequestManager
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.BaseViewModel
import com.pbl.mobile.extension.observeOnUiThread
import com.pbl.mobile.model.remote.image.UploadImageResponse
import com.pbl.mobile.model.remote.user.GetUserDetailResponse
import com.pbl.mobile.model.remote.user.UpdateUserAvatarBody
import com.pbl.mobile.model.remote.user.UpdateUserProfileBody
import com.pbl.mobile.model.remote.user.UpdateUserProfileResponse
import com.pbl.mobile.model.remote.user.password.ChangePasswordBody
import com.pbl.mobile.model.remote.user.password.ChangePasswordResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream


class EditProfileViewModel(val input: BaseInput.EditProFileInput) : BaseViewModel(input) {
    private val imageRequestManager = ImageRequestManager()
    private val userRequestManager = UserRequestManager()

    private val _uploadFrontImageResponse: MutableLiveData<BaseResponse<UploadImageResponse>?> =
        MutableLiveData()
    private val _uploadBackImageResponse: MutableLiveData<BaseResponse<UploadImageResponse>?> =
        MutableLiveData()
    private val _uploadAvatarResponse: MutableLiveData<BaseResponse<UploadImageResponse>?> =
        MutableLiveData()
    private val _updateUserProfileResponse: MutableLiveData<BaseResponse<UpdateUserProfileResponse>> =
        MutableLiveData()
    private val _updateUserAvatarResponse: MutableLiveData<BaseResponse<UpdateUserProfileResponse>> =
        MutableLiveData()
    private val _userDetailsResponse: MutableLiveData<BaseResponse<GetUserDetailResponse>> =
        MutableLiveData()
    private val _changePasswordResponse: MutableLiveData<BaseResponse<ChangePasswordResponse>> =
        MutableLiveData()

    fun uploadFrontImageResponse(): LiveData<BaseResponse<UploadImageResponse>?> =
        _uploadFrontImageResponse

    fun uploadBackImageResponse(): LiveData<BaseResponse<UploadImageResponse>?> =
        _uploadBackImageResponse

    fun uploadAvatarResponse(): LiveData<BaseResponse<UploadImageResponse>?> =
        _uploadAvatarResponse

    fun updateUserProfileResponse(): LiveData<BaseResponse<UpdateUserProfileResponse>?> =
        _updateUserProfileResponse

    fun updateUserAvatarResponse(): LiveData<BaseResponse<UpdateUserProfileResponse>?> =
        _updateUserAvatarResponse

    fun userDetailResponse(): LiveData<BaseResponse<GetUserDetailResponse>> =
        _userDetailsResponse

    fun changePasswordResponse(): LiveData<BaseResponse<ChangePasswordResponse>> =
        _changePasswordResponse

    fun uploadFrontImage(bitmap: Bitmap) {
        val imageParts = createImageParts(bitmap)
        subscription.add(
            imageRequestManager.uploadFrontImage(
                input.application,
                imageParts
            )
                .observeOnUiThread()
                .doOnSubscribe {
                    _uploadFrontImageResponse.value = BaseResponse.Loading()
                }
                .subscribe(
                    { uploadImageResponse ->
                        _uploadFrontImageResponse.value = BaseResponse.Success(uploadImageResponse)
                    },
                    { throwable ->
                        _uploadFrontImageResponse.value = BaseResponse.Error(throwable.message)
                    }
                )
        )
    }

    fun uploadBackImage(bitmap: Bitmap) {
        val imageParts = createImageParts(bitmap)
        subscription.add(
            imageRequestManager.uploadBackImage(
                input.application,
                imageParts
            )
                .observeOnUiThread()
                .doOnSubscribe {
                    _uploadBackImageResponse.value = BaseResponse.Loading()
                }
                .subscribe(
                    { uploadImageResponse ->
                        _uploadBackImageResponse.value = BaseResponse.Success(uploadImageResponse)
                    },
                    { throwable ->
                        _uploadBackImageResponse.value = BaseResponse.Error(throwable.message)
                    }
                )
        )
    }

    fun uploadAvatar(bitmap: Bitmap) {
        val imageParts = createImageParts(bitmap)
        subscription.add(
            imageRequestManager.uploadAvatar(
                input.application,
                imageParts
            )
                .observeOnUiThread()
                .doOnSubscribe {
                    _uploadAvatarResponse.value = BaseResponse.Loading()
                }
                .subscribe(
                    { uploadImageResponse ->
                        _uploadAvatarResponse.value = BaseResponse.Success(uploadImageResponse)
                    },
                    { throwable ->
                        _uploadAvatarResponse.value = BaseResponse.Error(throwable.message)
                    }
                )
        )
    }

    fun updateUserProfile(profile: UpdateUserProfileBody) {
        subscription.add(
            userRequestManager.updateUserProfile(
                input.application,
                profile
            )
                .observeOnUiThread()
                .subscribe(
                    { userProfileResponse ->
                        _updateUserProfileResponse.value = BaseResponse.Success(userProfileResponse)
                    },
                    { throwable ->
                        _updateUserProfileResponse.value = BaseResponse.Error(throwable.message)
                    }
                )
        )
    }

    fun updateUserAvatar(url: String) {
        subscription.add(
            userRequestManager.updateUserAvatar(
                input.application,
                UpdateUserAvatarBody(url)
            )
                .observeOnUiThread()
                .subscribe(
                    { avatarResponse ->
                        _updateUserAvatarResponse.value = BaseResponse.Success(avatarResponse)
                    },
                    { throwable ->
                        _updateUserAvatarResponse.value = BaseResponse.Error(throwable.message)
                    }
                )
        )
    }

    fun getUserDetail() {
        subscription.add(
            userRequestManager.getUserDetail(input.application)
                .observeOnUiThread()
                .doOnSubscribe {
                    _userDetailsResponse.value = BaseResponse.Loading()
                }
                .subscribe(
                    { userDetailResponse ->
                        _userDetailsResponse.value = BaseResponse.Success(userDetailResponse)
                    },
                    { throwable ->
                        _userDetailsResponse.value = BaseResponse.Error(throwable.message)
                    }
                )
        )
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        subscription.add(
            userRequestManager.changePassword(
                input.application,
                ChangePasswordBody(oldPassword, newPassword)
            )
                .observeOnUiThread()
                .doOnSubscribe {
                    _changePasswordResponse.value = BaseResponse.Loading()
                }
                .subscribe(
                    { changePasswordResponse ->
                        _changePasswordResponse.value = BaseResponse.Success(changePasswordResponse)
                    },
                    { throwable ->
                        _changePasswordResponse.value = BaseResponse.Error(throwable.message)
                    }
                )
        )
    }

    fun resetUploadedImageValue() {
        _uploadFrontImageResponse.value = null
        _uploadBackImageResponse.value = null
        _uploadAvatarResponse.value = null
    }

    private fun createImageParts(bitmap: Bitmap): MultipartBody.Part {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        val requestBody: RequestBody =
            imageBytes.toRequestBody("multipart/form-data".toMediaTypeOrNull(), 0, imageBytes.size)
        return MultipartBody.Part.createFormData("file", "image.jpg", requestBody)
    }
}
