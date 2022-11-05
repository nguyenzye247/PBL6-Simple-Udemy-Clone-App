package com.pbl.mobile.api.confirm

import android.app.Application
import com.pbl.mobile.model.remote.confirm.ConfirmResponse

class AccountConfirmRequestManager {
    fun confirm(application: Application, confirmCode: String): ConfirmResponse {
        return AccountConfirmApi.getApi(application).confirm(confirmCode)
    }
}
