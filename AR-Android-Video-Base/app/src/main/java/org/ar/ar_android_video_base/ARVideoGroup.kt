package org.ar.ar_android_video_base

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.Log
import android.view.TextureView
import android.view.View
import android.widget.RelativeLayout
import org.webrtc.PercentFrameLayout

class ARVideoGroup(activity: Activity, m_rl_video_group: RelativeLayout) {

    //所有视频View的容器
    var m_rl_video_group: RelativeLayout? = m_rl_video_group
    //所有视频的集合
    var m_list_video:LinkedHashMap<String, VideoView>?=null
    //视频View的宽
    private var VIDEO_WIDTH:Int =0
    //视频View的高
    private var VIDEO_HEIGHT:Int=0
    //屏幕宽
    private var SCREEN_WIDTH:Int =0
    //屏幕高
    private var SCREEN_HEIGHT:Int =0

    init {
        m_list_video = LinkedHashMap()
        val size = Point()
        val display = activity.getWindowManager().defaultDisplay
        display.getRealSize(size)
        SCREEN_WIDTH = size.x
        SCREEN_HEIGHT = size.y
        ResetVideoSize()
    }

    //一个VideoView 就是一个装了视频View对象
    inner class VideoView(videoView:TextureView) {
        var videoView:TextureView?=null
        var mLayout:PercentFrameLayout?=null
        private var context:Context?=null
        private var fl_video:RelativeLayout?=null
        var index :Int =0//下标 用于计算位置

        init {
            this.videoView = videoView
            this.context = videoView.getContext()
            mLayout =PercentFrameLayout(context)
            mLayout?.setLayoutParams(RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT))
            val view = View.inflate(context, R.layout.layout_video, null)//这个View可完全自定义 需要显示名字或者其他图标可以在里面加
            fl_video = view.findViewById(R.id.fl_video)
            fl_video?.addView(videoView)//将视频View添加到布局中
            mLayout?.addView(view)//添加到百分比布局中
        }
    }

    fun addView(vieoId:String,video:TextureView) {
        val videoView = VideoView(video)
        m_list_video?.put(vieoId, videoView)
        m_rl_video_group?.addView(videoView.mLayout)//将布局添加到从外面传进来的布局中
        updateLayout()
    }


    fun removeView(vieoId:String) {
        if (m_list_video?.containsKey(vieoId)!!) {
            m_rl_video_group?.removeView(m_list_video?.get(vieoId)?.mLayout)
            m_rl_video_group?.requestLayout()
        }
        m_list_video?.remove(vieoId)
        updateLayout()
    }

    fun removeAllView() {
        m_rl_video_group?.removeAllViews()
        m_list_video?.clear()
    }

    fun ResetVideoSize() {
        //4:3比例
        if (m_list_video?.size!! <= 4) {
            VIDEO_WIDTH = ((SCREEN_WIDTH / 2) / (SCREEN_WIDTH / 100f)).toInt()
            VIDEO_HEIGHT = ((SCREEN_WIDTH / 2) / (SCREEN_WIDTH / 100f)).toInt()
        } else {
            if (m_list_video?.size!! <= 9) {
                VIDEO_WIDTH = ((SCREEN_WIDTH / 3) / (SCREEN_WIDTH / 100f)).toInt()
                VIDEO_HEIGHT = ((SCREEN_WIDTH / 3) / (SCREEN_WIDTH / 100f)).toInt()
            } else if (m_list_video?.size!! <= 16){
                VIDEO_WIDTH =((SCREEN_WIDTH / 4) / (SCREEN_WIDTH / 100f)).toInt()
                VIDEO_HEIGHT = ((SCREEN_WIDTH / 4) / (SCREEN_WIDTH / 100f)).toInt()
            }else if (m_list_video?.size!! <= 25){
                VIDEO_WIDTH = ((SCREEN_WIDTH / 5) / (SCREEN_WIDTH / 100f)).toInt()
                VIDEO_HEIGHT = ((SCREEN_WIDTH / 5) / (SCREEN_WIDTH / 100f)).toInt()
            }else if (m_list_video?.size!! <= 36){
                VIDEO_WIDTH = ((SCREEN_WIDTH / 6) / (SCREEN_WIDTH / 100f)).toInt()
                VIDEO_HEIGHT = ((SCREEN_WIDTH / 6) / (SCREEN_WIDTH / 100f)).toInt()
            }else {
                VIDEO_WIDTH = ((SCREEN_WIDTH / 10) / (SCREEN_WIDTH / 100f)).toInt()
                VIDEO_HEIGHT = ((SCREEN_WIDTH / 10) / (SCREEN_WIDTH / 100f)).toInt()

            }
        }
    }
    fun updateLayout() {
        ResetVideoSize()
        //改变位置 大小 自己修改
        val list : List<Map.Entry<String, VideoView>> = ArrayList(m_list_video?.entries!!)
        list.forEachIndexed { index, entry -> //排序
            list.get(index).value.index =index
        }
        if (m_list_video?.size!!<= 4) {//1个本地像和1个远程像
            if (list.size==1 || list.size==2){
                val iter:Iterator<Map.Entry<String, VideoView>> = m_list_video?.entries!!.iterator()
                while (iter.hasNext()) {
                    val entry : Map.Entry<String, VideoView>  = iter.next()
                    val render :VideoView  = entry.value
                    if (render.index == 0) {
                        render.mLayout?.setPosition(0, 0, VIDEO_WIDTH, VIDEO_HEIGHT);
                    } else {
                        render.mLayout?.setPosition(VIDEO_WIDTH, 0, VIDEO_WIDTH, VIDEO_HEIGHT);
                    }
                    render.mLayout?.requestLayout();
                }
            }else if (list.size==3){
                val iter:Iterator<Map.Entry<String, VideoView>> = m_list_video?.entries!!.iterator()
                while (iter.hasNext()) {
                    val entry : Map.Entry<String, VideoView>  = iter.next()
                    val render :VideoView  = entry.value
                    if (render.index == 0) {
                        render.mLayout?.setPosition(0, 0, VIDEO_WIDTH, VIDEO_HEIGHT)
                    } else if (render.index == 1) {
                        render.mLayout?.setPosition(VIDEO_WIDTH, 0, VIDEO_WIDTH, VIDEO_HEIGHT)
                    } else if (render.index == 2) {
                        render.mLayout?.setPosition(0,  VIDEO_HEIGHT, VIDEO_WIDTH, VIDEO_HEIGHT)
                    }
                    render.mLayout?.requestLayout()
                }
            }else if (list.size==4) {
                val iter:Iterator<Map.Entry<String, VideoView>> = m_list_video?.entries!!.iterator()
                while (iter.hasNext()) {
                    val entry : Map.Entry<String, VideoView>  = iter.next()
                    val render :VideoView  = entry.value
                    if (render.index == 0) {
                        render.mLayout?.setPosition(0, 0, VIDEO_WIDTH, VIDEO_HEIGHT)
                    } else if (render.index == 1) {
                        render.mLayout?.setPosition(VIDEO_WIDTH, 0, VIDEO_WIDTH, VIDEO_HEIGHT)
                    } else if (render.index == 2) {
                        render.mLayout?.setPosition(0,  VIDEO_HEIGHT, VIDEO_WIDTH, VIDEO_HEIGHT)
                    } else if (render.index == 3) {
                        render.mLayout?.setPosition(VIDEO_WIDTH, VIDEO_HEIGHT, VIDEO_WIDTH, VIDEO_HEIGHT)
                    }
                    render.mLayout?.requestLayout()
                }
            }
            Log.d("位置大小 小于4个时","WIDTH="+VIDEO_WIDTH+"HEIGHT="+VIDEO_HEIGHT);
            m_rl_video_group?.requestLayout();
        }else {
            if (m_list_video?.size!! <= 9) {//4个及以上
                val iter:Iterator<Map.Entry<String, VideoView>> = m_list_video?.entries!!.iterator()
                while (iter.hasNext()) {
                    val entry : Map.Entry<String, VideoView>  = iter.next()
                    val render :VideoView  = entry.value
                    val row:Int = render.index / 3
                    val col:Int = render.index % 3
                    val X:Int = VIDEO_WIDTH * col
                    val Y:Int = VIDEO_HEIGHT * row
                    render.mLayout?.setPosition(X, Y, VIDEO_WIDTH, VIDEO_HEIGHT)
                    render.mLayout?.requestLayout()
                    Log.d("位置大小 小于9个时","X="+X+"Y="+Y+"WIDTH="+VIDEO_WIDTH+"HEIGHT="+VIDEO_HEIGHT);
                }
                m_rl_video_group?.requestLayout()
            } else if (m_list_video?.size!! <= 16){
                val iter:Iterator<Map.Entry<String, VideoView>> = m_list_video?.entries!!.iterator()
                while (iter.hasNext()) {
                    val entry : Map.Entry<String, VideoView>  = iter.next()
                    val render :VideoView  = entry.value
                    val row:Int = render.index / 4
                    val col:Int = render.index % 4
                    val X:Int= VIDEO_WIDTH * col
                    val Y:Int = VIDEO_HEIGHT * row
                    render.mLayout?.setPosition(X, Y, VIDEO_WIDTH, VIDEO_HEIGHT)
                    render.mLayout?.requestLayout()
                    Log.d("位置大小 大于9个时","X="+X+"Y="+Y+"WIDTH="+VIDEO_WIDTH+"HEIGHT="+VIDEO_HEIGHT);
                }
                m_rl_video_group?.requestLayout()
            }else if (m_list_video?.size!! <= 25){
                val iter:Iterator<Map.Entry<String, VideoView>> = m_list_video?.entries!!.iterator()
                while (iter.hasNext()) {
                    val entry : Map.Entry<String, VideoView>  = iter.next()
                    val render :VideoView  = entry.value
                    val row:Int = render.index / 5;
                    val col:Int = render.index % 5;
                    val X:Int = VIDEO_WIDTH * col;
                    val Y:Int = VIDEO_HEIGHT * row;
                    render.mLayout?.setPosition(X, Y, VIDEO_WIDTH, VIDEO_HEIGHT);
                    render.mLayout?.requestLayout();
                    Log.d("位置大小 大于16个时","X="+X+"Y="+Y+"WIDTH="+VIDEO_WIDTH+"HEIGHT="+VIDEO_HEIGHT);
                }
                m_rl_video_group?.requestLayout();
            }else if (m_list_video?.size!! <= 36){
                val iter:Iterator<Map.Entry<String, VideoView>> = m_list_video?.entries!!.iterator()
                while (iter.hasNext()) {
                    val entry : Map.Entry<String, VideoView>  = iter.next()
                    val render :VideoView  = entry.value
                    val row:Int = render.index / 6
                    val col:Int = render.index % 6
                    val X:Int = VIDEO_WIDTH * col
                    val Y:Int = VIDEO_HEIGHT * row
                    render.mLayout?.setPosition(X, Y, VIDEO_WIDTH, VIDEO_HEIGHT)
                    render.mLayout?.requestLayout()
                    Log.d("位置大小 大于25个时","X="+X+"Y="+Y+"WIDTH="+VIDEO_WIDTH+"HEIGHT="+VIDEO_HEIGHT)
                }
                m_rl_video_group?.requestLayout()
            }else {
                val iter:Iterator<Map.Entry<String, VideoView>> = m_list_video?.entries!!.iterator()
                while (iter.hasNext()) {
                    val entry : Map.Entry<String, VideoView>  = iter.next()
                    val render :VideoView  = entry.value
                    val row:Int = render.index / 10
                    val col:Int = render.index % 10
                    val X:Int = VIDEO_WIDTH * col
                    val Y:Int= VIDEO_HEIGHT * row
                    render.mLayout?.setPosition(X, Y, VIDEO_WIDTH, VIDEO_HEIGHT);
                    render.mLayout?.requestLayout()
                    Log.d("位置大小 大于36个时","X="+X+"Y="+Y+"WIDTH="+VIDEO_WIDTH+"HEIGHT="+VIDEO_HEIGHT);
                }
                m_rl_video_group?.requestLayout();
            }
        }

    }
}