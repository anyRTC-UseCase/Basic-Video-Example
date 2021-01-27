package org.ar.ar_android_video_base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import org.ar.ar_android_video_base.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),View.OnClickListener{

    private var viewBinding: ActivityMainBinding? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding=ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding?.root)
        viewBinding?.join?.setOnClickListener(this)
        if (!AndPermission.hasPermissions(this, Permission.Group.STORAGE,
                        Permission.Group.MICROPHONE)){
            AndPermission.with(this)
                    .runtime()
                    .permission(
                    Permission.Group.STORAGE,
                    Permission.Group.MICROPHONE,
                    Permission.Group.CAMERA
            ).onGranted({}).start();
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.join->{
                if (!TextUtils.isEmpty(viewBinding?.input?.text.toString())){
                    Intent(this,VideoBaseActivity::class.java).let {
                        it.putExtra("channelId",viewBinding?.input?.text.toString())
                        startActivity(it)
                    }
                }else{
                    Toast.makeText(this, "请输入频道ID", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}