package com.pbl.mobile.notification

import android.util.Log
import com.onesignal.OSNotificationOpenedResult
import com.onesignal.OneSignal

class MyNotificationOpenHandler : OneSignal.OSNotificationOpenedHandler {
    override fun notificationOpened(result: OSNotificationOpenedResult?) {
        result?.let {
            val actionId: String = result.action.actionId
            val type: String = result.action.type.toString() // "ActionTaken" | "Opened"
            val title: String = result.notification.title

            Log.d("PUSH_NOTIFICATION_123", "actionId: $actionId ____ type: $type ___ title: $title")
        }

    }
}