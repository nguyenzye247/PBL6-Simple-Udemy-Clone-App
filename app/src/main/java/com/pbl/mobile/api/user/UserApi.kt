package com.pbl.mobile.api.user

import android.app.Application
import com.pbl.mobile.api.*
import com.pbl.mobile.model.remote.user.*
import com.pbl.mobile.model.remote.user.password.ChangePasswordBody
import com.pbl.mobile.model.remote.user.password.ChangePasswordResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
    companion object {
        fun getApi(application: Application): UserApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(UserApi::class.java)
        }
    }

    @GET("$GET_USER/{ID}")
    fun getSimpleUserById(@Path("ID") userId: String): Single<GetSimpleUserResponse>

    @GET(GET_ME)
    fun getMe(
        @Header("Authorization") token: String
    ): Single<GetMeResponse>

    @PUT(UPDATE_PROFILE_URL)
    fun updateUserInfo(
        @Body body: UpdateUserProfileBody
    ): Single<UpdateUserProfileResponse>

    @PUT(UPDATE_AVATAR_URL)
    fun updateUserAvatar(
        @Body avatarBody: UpdateUserAvatarBody
    ): Single<UpdateUserProfileResponse>

    @GET(GET_USER_DETAIL_URL)
    fun getUserDetails(): Single<GetUserDetailResponse>

    @POST(CHANGE_PASSWORD_URL)
    fun changePassword(@Body passwordBody: ChangePasswordBody): Single<ChangePasswordResponse>
}
