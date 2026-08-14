package com.example.mtaafix

import android.app.Application
import com.cloudinary.android.MediaManager

class MtaaFixApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = mapOf(
            "cloud_name" to "ficr02mq"
        )
        MediaManager.init(this, config)
    }
}