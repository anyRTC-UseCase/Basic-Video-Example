package org.ar.ar_android_video_base.java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import org.ar.ar_android_video_base.App;
import org.ar.ar_android_video_base.Member;
import org.ar.ar_android_video_base.R;
import org.ar.ar_android_video_base.databinding.ActivityVideoBinding;
import org.ar.rtc.IRtcEngineEventHandler;
import org.ar.rtc.RtcEngine;

public class JavaVideoActivity extends AppCompatActivity {

    private String channelId="";
    private String userId ="";
    private RtcEngine mRtcEngine;
    private JavaMemberAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        recyclerView = findViewById(R.id.rv_video);
        channelId = getIntent().getStringExtra("channelId");
        userId = App.Companion.getApp().getUserId();




        mRtcEngine = RtcEngine.create(this, getString(R.string.ar_appid), new IRtcEngineEventHandler() {
            @Override
            public void onJoinChannelSuccess(String channel, String uid, int elapsed) {
                super.onJoinChannelSuccess(channel, uid, elapsed);
                runOnUiThread(()->{
                    Member member = new Member(uid);
                    mRtcEngine.setupLocalVideo(member.getVideoCanvas(JavaVideoActivity.this));
                    adapter.addData(member);

                });
            }

            @Override
            public void onUserJoined(String uid, int elapsed) {
                super.onUserJoined(uid, elapsed);
                runOnUiThread(() -> {
                    Member member = new Member(uid);
                    mRtcEngine.setupRemoteVideo(member.getVideoCanvas(JavaVideoActivity.this));
                    adapter.addData(member);
                });
            }

            @Override
            public void onUserOffline(String uid, int reason) {
                super.onUserOffline(uid, reason);
                runOnUiThread(() -> {
                    adapter.remove(new Member(uid));
                });
            }
        });
        mRtcEngine.enableVideo();

        adapter = new JavaMemberAdapter(mRtcEngine,recyclerView);
        recyclerView.setAdapter(adapter);
        mRtcEngine.joinChannel("",channelId,"",userId);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RtcEngine.destroy();
    }
}