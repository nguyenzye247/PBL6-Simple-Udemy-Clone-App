package com.pbl.mobile.api.follower

import android.app.Application
import com.pbl.mobile.model.remote.follower.GetFollowerOfInstructorResponse
import io.reactivex.rxjava3.core.Single

class FollowerRequestManager {
    fun getFollowersOfInstructor(
        application: Application,
        instructorId: String,
        page: Int,
        paging: Int
    ): Single<GetFollowerOfInstructorResponse> {
        return FollowerApi.getApi(application).getFollowersOfInstructor(instructorId, page, paging)
    }
}
