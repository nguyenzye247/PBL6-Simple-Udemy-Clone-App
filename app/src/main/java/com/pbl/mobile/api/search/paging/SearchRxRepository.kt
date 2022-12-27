package com.pbl.mobile.api.search.paging

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.pbl.mobile.model.local.Course
import kotlinx.coroutines.flow.Flow

interface SearchRxRepository {
    fun search(searchCoursePagingSource: SearchCoursePagingSource): Flow<PagingData<Course>>
}
