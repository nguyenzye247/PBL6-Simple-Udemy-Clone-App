package com.pbl.mobile.api.search

import android.app.Application
import com.pbl.mobile.api.BaseRequestManager
import com.pbl.mobile.api.SEARCH_URL
import com.pbl.mobile.model.remote.search.SearchResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface SearchApi {
    companion object {
        fun getApi(application: Application): SearchApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(SearchApi::class.java)
        }
    }

    @GET(SEARCH_URL)
    fun searchCourses(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("key") key: String,
        @Query("lteq") minPrice: Long,
        @Query("gteq") maxPrice: Long,
        @Query("category") category: String,
        @Query("tag") tag: String
    ): Single<SearchResponse>
}
