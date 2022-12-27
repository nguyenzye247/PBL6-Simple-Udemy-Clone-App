package com.pbl.mobile.api.search.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pbl.mobile.model.local.Course
import kotlinx.coroutines.flow.Flow

class SearchRxRepositoryImpl : SearchRxRepository {
    override fun search(searchCoursePagingSource: SearchCoursePagingSource): Flow<PagingData<Course>> {
        return Pager(
            config = PagingConfig(
                pageSize = 4,
                maxSize = 50,
                prefetchDistance = 2,
                initialLoadSize = 3
            ),
            pagingSourceFactory = {
                searchCoursePagingSource
            }
        ).flow
    }
}