package org.ar.ar_android_video_base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import org.ar.ar_android_video_base.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding=ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)
        viewBinding.input.addTextChangedListener(textWatcher)
        viewBinding.join.isEnabled =false

        if (!AndPermission.hasPermissions(this, Permission.Group.STORAGE,
                        Permission.Group.MICROPHONE)){
            AndPermission.with(this)
                    .runtime()
                    .permission(
                    Permission.Group.STORAGE,
                    Permission.Group.MICROPHONE,
                    Permission.Group.CAMERA
            ).onGranted {}.start();
        }

        viewBinding.join.setOnClickListener {
            Intent(this,VideoActivity::class.java).apply {
                putExtra("channelId",viewBinding.input.text.toString())
                startActivity(this)
            }
        }
    }

    private var textWatcher: TextWatcher =object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            p0?.let {
                viewBinding.join.isEnabled = !it.toString().isNullOrEmpty()
            }
        }
    }

}