package com.pbl.mobile.api.section

import android.app.Application
import com.pbl.mobile.api.BaseRequestManager
import com.pbl.mobile.api.GET_COURSES_SECTION_URL
import com.pbl.mobile.api.GET_COURSES_URL
import com.pbl.mobile.model.remote.section.GetSectionsResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SectionApi {
    companion object {
        fun getApi(application: Application): SectionApi {
            return BaseRequestManager.getInstance(application).myRetrofit.create(SectionApi::class.java)
        }
    }

    @GET("$GET_COURSES_URL/{ID}/$GET_COURSES_SECTION_URL")
    fun getCourseSections(
        @Path("ID") courseId: String
    ): Single<GetSectionsResponse>
}
