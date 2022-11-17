package com.pbl.mobile.api.courses

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.pbl.mobile.api.courses.paging.CoursePagingSource
import com.pbl.mobile.api.courses.paging.GetCoursesRxRepository
import com.pbl.mobile.model.local.Course

class GetCoursesRxRepositoryImpl(
    private val coursePagingSource: CoursePagingSource
) : GetCoursesRxRepository {
    override fun getCourses(): LiveData<PagingData<Course>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 50,
                prefetchDistance = 5,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                coursePagingSource
            }
        ).liveData
    }
}
