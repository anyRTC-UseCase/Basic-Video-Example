package org.ar.ar_android_video_base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
        viewBinding?.input?.addTextChangedListener(textWatcher)
        viewBinding?.join?.setOnClickListener(this)
        viewBinding?.join?.isEnabled =false
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

    private var textWatcher: TextWatcher =object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            viewBinding?.join?.isEnabled = p0?.toString()?.length!! > 0
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.join->{
                Intent(this,VideoBaseActivity::class.java).let {
                    it.putExtra("channelId",viewBinding?.input?.text.toString())
                    startActivity(it)
                }
            }
        }
    }

}