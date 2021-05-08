package org.ar.ar_android_video_base

import android.content.Context
import android.view.ViewGroup
import org.ar.rtc.RtcEngine
import org.ar.rtc.video.VideoCanvas

class Member(var uid: String) {

    var canvas:VideoCanvas? = null

    fun getVideoCanvas(context: Context?): VideoCanvas? {
        if (canvas == null) {
            canvas = VideoCanvas(RtcEngine.CreateRendererView(context))
            canvas?.uid = uid
        }
        return canvas
    }


    override fun equals(other: Any?): Boolean {
        if (other !is Member){
            return false
        }
        return (other.uid == this.uid)
    }



}