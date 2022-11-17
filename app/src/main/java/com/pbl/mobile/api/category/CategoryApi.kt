package com.pbl.mobile.api.category

import android.app.Application
import com.pbl.mobile.api.*
import com.pbl.mobile.model.remote.category.get.CategoryGetResponse
import com.pbl.mobile.model.remote.category.post.CategoryCreateRequest
import com.pbl.mobile.model.remote.category.post.CategoryCreateResponse
import com.pbl.mobile.model.remote.category.put.CategoryEditResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface CategoryApi {
    companion object {
        fun getApi(application: Application): CategoryApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(CategoryApi::class.java)
        }
    }

    @POST(CREATE_CATEGORY_URL)
    fun create(@Body request: CategoryCreateRequest): Single<CategoryCreateResponse>

    @GET(GET_CATEGORY_URL)
    fun getAll(@Query("page") page: Int = -1): Single<CategoryGetResponse>

    @POST(EDIT_CATEGORY_URL)
    fun update(@Body name: String): Single<CategoryEditResponse>

    @DELETE("$DELETE_CATEGORY_URL/{ID}")
    fun delete(@Path("ID") id: String)
}
