package com.pbl.mobile.api.user

import android.app.Application
import com.pbl.mobile.api.BEARER
import com.pbl.mobile.model.remote.user.*
import com.pbl.mobile.model.remote.user.password.ChangePasswordBody
import com.pbl.mobile.model.remote.user.password.ChangePasswordResponse
import io.reactivex.rxjava3.core.Single

class UserRequestManager {
    fun getSimpleUserById(application: Application, userId: String): Single<GetSimpleUserResponse> {
        return UserApi.getApi(application).getSimpleUserById(userId)
    }

    fun getMe(application: Application, token: String): Single<GetMeResponse> {
        return UserApi.getApi(application).getMe(BEARER + token)
    }

    fun updateUserProfile(
        application: Application,
        body: UpdateUserProfileBody
    ): Single<UpdateUserProfileResponse> {
        return UserApi.getApi(application).updateUserInfo(body)
    }

    fun updateUserAvatar(
        application: Application,
        avatarBody: UpdateUserAvatarBody
    ): Single<UpdateUserProfileResponse> {
        return UserApi.getApi(application).updateUserAvatar(avatarBody)
    }

    fun getUserDetail(application: Application): Single<GetUserDetailResponse> {
        return UserApi.getApi(application).getUserDetails()
    }

    fun changePassword(
        application: Application,
        changePasswordBody: ChangePasswordBody
    ): Single<ChangePasswordResponse> {
        return UserApi.getApi(application).changePassword(changePasswordBody)
    }
}
