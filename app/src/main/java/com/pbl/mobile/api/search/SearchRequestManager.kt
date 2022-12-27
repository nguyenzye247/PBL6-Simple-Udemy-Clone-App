package com.pbl.mobile.api.search

import android.app.Application
import com.pbl.mobile.model.remote.search.SearchResponse
import io.reactivex.rxjava3.core.Single

class SearchRequestManager {
    fun searchCourses(
        application: Application,
        page: Int,
        limit: Int,
        key: String,
        minPrice: Long,
        maxPrice: Long,
        category: String,
        tag: String
    ): Single<SearchResponse> {
        return SearchApi.getApi(application).searchCourses(page, limit, key, minPrice, maxPrice, category, tag)
    }
}
