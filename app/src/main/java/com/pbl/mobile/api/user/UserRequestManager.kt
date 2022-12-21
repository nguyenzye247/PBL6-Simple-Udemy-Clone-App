package com.pbl.mobile.api.user

import android.app.Application
import com.pbl.mobile.api.BEARER
import com.pbl.mobile.model.remote.user.GetMeResponse
import com.pbl.mobile.model.remote.user.GetSimpleUserResponse
import io.reactivex.rxjava3.core.Single

class UserRequestManager {
    fun getSimpleUserById(application: Application, userId: String): Single<GetSimpleUserResponse> {
        return UserApi.getApi(application).getSimpleUserById(userId)
    }

    fun getMe(application: Application, token: String): Single<GetMeResponse> {
        return UserApi.getApi(application).getMe(BEARER + token)
    }
}
