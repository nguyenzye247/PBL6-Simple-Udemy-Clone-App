package com.pbl.mobile.api.hashtag

import android.app.Application
import com.pbl.mobile.model.remote.hashtag.GetHashtagResponse
import io.reactivex.rxjava3.core.Single

class HashtagRequestManager {

    fun getHashtags(application: Application): Single<GetHashtagResponse> {
        return HashtagApi.getApi(application).getAll()
    }
}