package com.pbl.mobile

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.onesignal.OneSignal
import com.pbl.mobile.common.ONESIGNAL_APP_ID
import com.pbl.mobile.databinding.ActivityMainBinding
import com.pbl.mobile.extension.getBaseConfig
import com.pbl.mobile.ui.signin.SignInActivity

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onesignalSetup()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
        }, 3000)
    }

    private fun onesignalSetup(){
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        OneSignal.setExternalUserId(this.getBaseConfig().myId)
        OneSignal.unsubscribeWhenNotificationsAreDisabled(true)
    }
}