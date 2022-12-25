package com.pbl.mobile.api.follower

import android.app.Application
import com.pbl.mobile.api.BaseRequestManager
import com.pbl.mobile.api.FOLLOW_URL
import com.pbl.mobile.api.GET_INSTRUCTOR_FOLLOWER_URL
import com.pbl.mobile.model.remote.follower.GetFollowerOfInstructorResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FollowerApi {
    companion object {
        fun getApi(application: Application): FollowerApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(FollowerApi::class.java)
        }
    }

    @GET("$GET_INSTRUCTOR_FOLLOWER_URL/{instructorId}/$FOLLOW_URL")
    fun getFollowersOfInstructor(
        @Path("instructorId") instructorId: String,
        @Query("page") page: Int,
        @Query("paging") paging: Int,
        @Query("sort") sort: String = "createdAt",
        @Query("order") order: String = "asc"
    ): Single<GetFollowerOfInstructorResponse>
}
