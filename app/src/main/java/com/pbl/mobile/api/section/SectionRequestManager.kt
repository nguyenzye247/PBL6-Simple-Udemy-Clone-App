package com.pbl.mobile.api.section

import android.app.Application
import com.pbl.mobile.model.remote.section.GetSectionsResponse
import io.reactivex.rxjava3.core.Single

class SectionRequestManager {
    fun getCourseSections(
        application: Application,
//        token: String,
        courseId: String
    ): Single<GetSectionsResponse> {
        return SectionApi.getApi(application).getCourseSections(
//            token,
            courseId
        )
    }
}
