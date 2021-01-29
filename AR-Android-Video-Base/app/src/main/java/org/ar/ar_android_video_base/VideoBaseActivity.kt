package org.ar.ar_android_video_base

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_video.*
import org.ar.ar_android_video_base.databinding.ActivityVideoBinding
import org.ar.rtc.Constants
import org.ar.rtc.IRtcEngineEventHandler
import org.ar.rtc.RtcEngine
import org.ar.rtc.VideoEncoderConfiguration
import org.ar.rtc.video.VideoCanvas

class VideoBaseActivity:AppCompatActivity() ,View.OnClickListener{

    private val TAG = VideoBaseActivity::class.java.simpleName
    private var viewBinding:ActivityVideoBinding? =null
    private var channelId:String =""
    private var userId:String =""
    private var mRtcEngine:RtcEngine? =null
    private var isMic:Boolean=false
    private var isCamera:Boolean=false
    private var arVideoGroup:ARVideoGroup? =null

    private inner class mRtcEvent :IRtcEngineEventHandler(){
        override fun onJoinChannelSuccess(channel: String?, uid: String?, elapsed: Int) {
            super.onJoinChannelSuccess(channel, uid, elapsed)
            runOnUiThread {
                Log.i(TAG, "onJoinChannelSuccess: --->")
            }
        }

        override fun onUserJoined(uid: String?, elapsed: Int) {
            super.onUserJoined(uid, elapsed)
            runOnUiThread {
                Log.i(TAG, "onUserJoined: --->")
            }
        }

        override fun onFirstLocalVideoFrame(width: Int, height: Int, elapsed: Int) {
            super.onFirstLocalVideoFrame(width, height, elapsed)
            runOnUiThread {
                setupLocalVideo()
            }
        }

        override fun onFirstRemoteVideoDecoded(uid: String?, width: Int, height: Int, elapsed: Int) {
            super.onFirstRemoteVideoDecoded(uid, width, height, elapsed)
            runOnUiThread {
                Log.i(TAG, "onFirstRemoteVideoDecoded: --->")
                setupRemoteVideo(uid!!)
            }
        }

        override fun onUserOffline(uid: String?, reason: Int) {
            super.onUserOffline(uid, reason)
            runOnUiThread {
                arVideoGroup?.removeView(uid!!);
                Log.i(TAG, "onUserOffline: --->")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityVideoBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding?.root)
        channelId =intent?.getStringExtra("channelId").toString()
        userId = VideoApp.videoApp.userId.toString()
        arVideoGroup = ARVideoGroup(this,viewBinding?.rlVideo!!)
        viewBinding?.mic?.setOnClickListener(this)
        viewBinding?.camera?.setOnClickListener(this)
        viewBinding?.leave?.setOnClickListener(this)
        joinChannel()
    }

    private fun joinChannel() {
        mRtcEngine = RtcEngine.create(this,getString(R.string.ar_appid),mRtcEvent())
        mRtcEngine?.enableVideo()
        mRtcEngine?.setVideoEncoderConfiguration(VideoEncoderConfiguration(
            VideoEncoderConfiguration.VD_640x480,
            VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
            VideoEncoderConfiguration.STANDARD_BITRATE,
            VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT))
        mRtcEngine?.setEnableSpeakerphone(true)
        mRtcEngine?.joinChannel(getString(R.string.ar_token),channelId,"",userId)
    }

    private fun setupLocalVideo(){
        val view =RtcEngine.CreateRendererView(baseContext)
        if (viewBinding?.rlVideo !=null){
            rl_video.removeAllViews()
        }
        arVideoGroup?.addView(userId,view)
        mRtcEngine?.setupLocalVideo(VideoCanvas(view,VideoCanvas.RENDER_MODE_HIDDEN,userId))
        mRtcEngine?.startPreview()
    }

    private fun removeLocal(){
        arVideoGroup?.removeView(userId);
    }

    private fun setupRemoteVideo(uid:String){
        val view =RtcEngine.CreateRendererView(baseContext)
        arVideoGroup?.addView(uid,view)
        mRtcEngine?.setupRemoteVideo(VideoCanvas(view,Constants.RENDER_MODE_HIDDEN,uid))
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            release()
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.mic ->{
                isMic =!isMic
                viewBinding?.mic?.isSelected =isMic
                mRtcEngine?.muteLocalAudioStream(isMic)
            }
            R.id.camera->{
                isCamera =!isCamera
                viewBinding?.camera?.isSelected=isCamera
                mRtcEngine?.switchCamera()
            }
            R.id.leave->{
                release()
                finish()
            }
        }
    }

    private fun release(){
        removeLocal()
        mRtcEngine?.leaveChannel()
        RtcEngine.destroy()
        mRtcEngine=null
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

}