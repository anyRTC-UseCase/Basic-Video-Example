package org.ar.ar_android_video_base

import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import org.ar.rtc.RtcEngine
import org.ar.rtc.video.VideoCanvas

class MemberAdapter(rtcEngine: RtcEngine) : BaseQuickAdapter<Member, BaseViewHolder>(R.layout.layout_video) {

    private var rtcEngine = rtcEngine

    override fun convert(baseViewHolder: BaseViewHolder, member: Member) {
        val root = baseViewHolder.getView<RelativeLayout>(R.id.rl_root)
        val canvas = member.getVideoCanvas(baseViewHolder.itemView.context)
        canvas?.let {
            it.view.parent?.let {
                (canvas.getView().parent as ViewGroup).removeView(canvas.view)
            }
        }
        if (member.uid==App.app.userId) {
            if (!canvas?.view?.isAvailable!!){//防止本地视图黑屏 这里给他重新设置一下View
                member.canvas= VideoCanvas(RtcEngine.CreateRendererView(baseViewHolder.itemView.context))
                rtcEngine.setupLocalVideo(member.canvas)
            }
        }
        val layoutManager = weakRecyclerView.get()?.layoutManager as GridLayoutManager
        val width = layoutManager.width / layoutManager.spanCount.toDouble()
        val height = width * 1.33333333f
        val params = ViewGroup.LayoutParams(width.toInt(), height.toInt())
        root.layoutParams = params
        root.addView(member.canvas?.view)
    }

    override fun addData(data: Member) {
        when {
            itemCount+1<3 -> {
                weakRecyclerView.get()?.let {
                    it.layoutManager=GridLayoutManager(it.context,2,GridLayoutManager.VERTICAL,false)
                }
            }
            itemCount+1<=9 -> {
                weakRecyclerView.get()?.let {
                    it.layoutManager=GridLayoutManager(it.context,3,GridLayoutManager.VERTICAL,false)
                }
            }
            else -> {
                weakRecyclerView.get()?.let {
                    it.layoutManager=GridLayoutManager(it.context,4,GridLayoutManager.VERTICAL,false)
                }
            }
        }
        super.addData(data)
    }


    override fun remove(data: Member) {
        when {
            itemCount-1<3 -> {
                weakRecyclerView.get()?.let {
                    it.layoutManager=GridLayoutManager(it.context,2,GridLayoutManager.VERTICAL,false)
                }
            }
            itemCount-1<=9 -> {
                weakRecyclerView.get()?.let {
                    it.layoutManager=GridLayoutManager(it.context,3,GridLayoutManager.VERTICAL,false)
                }
            }
            else -> {
                weakRecyclerView.get()?.let {
                    it.layoutManager=GridLayoutManager(it.context,4,GridLayoutManager.VERTICAL,false)
                }
            }
        }
        super.remove(data)
    }
}