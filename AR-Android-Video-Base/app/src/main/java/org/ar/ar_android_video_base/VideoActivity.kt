package org.ar.ar_android_video_base

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import org.ar.ar_android_video_base.databinding.ActivityVideoBinding
import org.ar.rtc.Constants
import org.ar.rtc.IRtcEngineEventHandler
import org.ar.rtc.RtcEngine
import org.ar.rtc.video.VideoCanvas

class VideoActivity:AppCompatActivity() ,View.OnClickListener{

    private val TAG = VideoActivity::class.java.simpleName
    private lateinit var viewBinding:ActivityVideoBinding
    private var channelId:String =""
    private var userId:String =""
    private var mRtcEngine:RtcEngine? =null
    private var isMic:Boolean=false
    private var isCamera:Boolean=false
    private lateinit var videoAdapter:MemberAdapter

    private inner class RtcEvent :IRtcEngineEventHandler(){
        override fun onJoinChannelSuccess(channel: String, uid: String, elapsed: Int) {
            super.onJoinChannelSuccess(channel, uid, elapsed)
            runOnUiThread {
                Log.i(TAG, "onJoinChannelSuccess: --->")
                val member = Member(uid)
                mRtcEngine?.setupLocalVideo(member.getVideoCanvas(this@VideoActivity))
                videoAdapter.addData(member)
            }
        }

        override fun onUserJoined(uid: String, elapsed: Int) {
            super.onUserJoined(uid, elapsed)
            runOnUiThread {
                Log.i(TAG, "onUserJoined: --->")
            }
        }

        override fun onFirstRemoteVideoDecoded(uid: String, width: Int, height: Int, elapsed: Int) {
            super.onFirstRemoteVideoDecoded(uid, width, height, elapsed)
            runOnUiThread {
                Log.i(TAG, "onFirstRemoteVideoDecoded: --->")
                val member = Member(uid)
                mRtcEngine?.setupRemoteVideo(member.getVideoCanvas(this@VideoActivity))
                videoAdapter.addData(member)
            }
        }

        override fun onUserOffline(uid: String, reason: Int) {
            super.onUserOffline(uid, reason)
            runOnUiThread {
                Log.i(TAG, "onUserOffline: --->")
                videoAdapter.remove(Member(uid))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityVideoBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)
        channelId = intent.getStringExtra("channelId").toString()
        userId = App.app.userId.toString()
        mRtcEngine = RtcEngine.create(this,getString(R.string.ar_appid),RtcEvent())
        initView()
        joinChannel()
    }

    private fun initView(){
        videoAdapter = MemberAdapter(mRtcEngine!!)
        viewBinding.rvVideo.layoutManager = GridLayoutManager(this,2)
        viewBinding.rvVideo.adapter = videoAdapter
        viewBinding.mic.setOnClickListener(this)
        viewBinding.camera.setOnClickListener(this)
        viewBinding.leave.setOnClickListener(this)
    }

    private fun joinChannel(){
        mRtcEngine?.let {
            it.enableVideo()
            it.setEnableSpeakerphone(true)
            it.joinChannel(getString(R.string.ar_token),channelId,"",userId)
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.mic ->{
                isMic =!isMic
                viewBinding.mic.isSelected =isMic
                mRtcEngine?.muteLocalAudioStream(isMic)
            }
            R.id.camera->{
                isCamera =!isCamera
                viewBinding.camera.isSelected=isCamera
                mRtcEngine?.switchCamera()
            }
            R.id.leave->{
                finish()
            }
        }
    }

    private fun release(){
        mRtcEngine?.leaveChannel()
        RtcEngine.destroy()
        mRtcEngine=null
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

}