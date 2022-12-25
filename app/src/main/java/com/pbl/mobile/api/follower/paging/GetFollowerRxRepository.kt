package com.pbl.mobile.api.follower.paging

import androidx.paging.PagingData
import com.pbl.mobile.api.follower.paging.source.FollowerPagingSource
import com.pbl.mobile.model.remote.follower.GetFollowerOfInstructorResponse
import kotlinx.coroutines.flow.Flow

interface GetFollowerRxRepository {
    fun getFollowersOfInstructor(followerPagingSource: FollowerPagingSource): Flow<PagingData<GetFollowerOfInstructorResponse.Data>>
}
