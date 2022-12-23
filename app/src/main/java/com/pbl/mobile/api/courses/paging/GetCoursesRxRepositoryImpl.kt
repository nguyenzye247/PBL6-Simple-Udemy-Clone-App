package com.pbl.mobile.api.courses.paging

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.pbl.mobile.api.courses.paging.source.CoursePagingSource
import com.pbl.mobile.model.local.Course

class GetCoursesRxRepositoryImpl(
    private val coursePagingSource: CoursePagingSource
) : GetCoursesRxRepository {
    override fun getCourses(): LiveData<PagingData<Course>> {
        return Pager(
            config = PagingConfig(
                pageSize = 4,
                maxSize = 100,
                prefetchDistance = 2,
                initialLoadSize = 3
            ),
            pagingSourceFactory = {
                coursePagingSource
            }
        ).liveData
    }

    override fun getInstructorCourses(): LiveData<PagingData<Course>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 100,
                prefetchDistance = 3,
                initialLoadSize = 5
            ),
            pagingSourceFactory = {
                coursePagingSource
            }
        ).liveData
    }
}
