package org.ar.ar_android_video_base

import android.app.Application
import kotlin.properties.Delegates

class VideoApp:Application() {

    val userId :String? =((Math.random()*9+1)*100000L).toInt().toString()

    companion object{
        var videoApp: VideoApp by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        videoApp = this
        // DialogSettings.style = DialogSettings.STYLE.STYLE_IOS
    }
}