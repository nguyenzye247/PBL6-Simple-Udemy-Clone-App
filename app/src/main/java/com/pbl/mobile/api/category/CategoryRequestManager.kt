package com.pbl.mobile.api.category

import android.app.Application
import com.pbl.mobile.model.remote.category.get.CategoryGetResponse
import com.pbl.mobile.model.remote.category.post.CategoryCreateRequest
import com.pbl.mobile.model.remote.category.post.CategoryCreateResponse
import com.pbl.mobile.model.remote.category.put.CategoryEditResponse
import io.reactivex.rxjava3.core.Single

class CategoryRequestManager {
    fun createCategory(
        application: Application,
        categoryRequest: CategoryCreateRequest
    ): Single<CategoryCreateResponse> {
        return CategoryApi.getApi(application).create(categoryRequest)
    }

    fun getCategories(application: Application): Single<CategoryGetResponse> {
        return CategoryApi.getApi(application).getAll()
    }

    fun update(application: Application, newName: String): Single<CategoryEditResponse> {
        return CategoryApi.getApi(application).update(newName)
    }

    fun delete(application: Application, id: String) {
        CategoryApi.getApi(application).delete(id)
    }
}
