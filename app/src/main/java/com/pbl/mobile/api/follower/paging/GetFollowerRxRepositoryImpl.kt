package com.pbl.mobile.api.follower.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pbl.mobile.api.follower.paging.source.FollowerPagingSource
import com.pbl.mobile.model.remote.follower.GetFollowerOfInstructorResponse
import kotlinx.coroutines.flow.Flow

class GetFollowerRxRepositoryImpl : GetFollowerRxRepository {
    override fun getFollowersOfInstructor(followerPagingSource: FollowerPagingSource): Flow<PagingData<GetFollowerOfInstructorResponse.Data>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 100,
                prefetchDistance = 7,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { followerPagingSource }
        ).flow
    }
}