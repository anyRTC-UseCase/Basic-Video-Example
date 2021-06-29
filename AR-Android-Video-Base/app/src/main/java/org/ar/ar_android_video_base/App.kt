package org.ar.ar_android_video_base

import android.app.Application
import kotlin.properties.Delegates

class App:Application() {

    val userId :String =((Math.random()*9+1)*100000L).toInt().toString()

    companion object{
        var app: App by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}