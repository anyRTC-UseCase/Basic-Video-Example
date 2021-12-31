package org.ar.ar_android_video_base.java;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.ar.ar_android_video_base.App;
import org.ar.ar_android_video_base.Member;
import org.ar.ar_android_video_base.R;
import org.ar.rtc.RtcEngine;
import org.ar.rtc.video.VideoCanvas;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class JavaMemberAdapter extends RecyclerView.Adapter<JavaMemberAdapter.ViewHolder> {

    private RtcEngine rtcEngine;
    private List<Member> memberList = new ArrayList<>();
    private WeakReference<RecyclerView> recyclerViewWeakReference;
    public JavaMemberAdapter(RtcEngine engine,RecyclerView recyclerView) {
        this.rtcEngine = engine;
        recyclerViewWeakReference = new WeakReference<>(recyclerView);
    }


    public void addData(@NonNull Member data) {
       if (getItemCount()+1<3){
          recyclerViewWeakReference.get().setLayoutManager(new GridLayoutManager(recyclerViewWeakReference.get().getContext(),2,GridLayoutManager.VERTICAL,false));
       }else if (getItemCount()+1<=9){
          recyclerViewWeakReference.get().setLayoutManager(new GridLayoutManager(recyclerViewWeakReference.get().getContext(),3,GridLayoutManager.VERTICAL,false));
       }else {
          recyclerViewWeakReference.get().setLayoutManager(new GridLayoutManager(recyclerViewWeakReference.get().getContext(),4,GridLayoutManager.VERTICAL,false));
       }
        memberList.add(data);
        notifyItemInserted(memberList.size());
        compatibilityDataSizeChanged(1);
    }

    public void remove(Member data) {
        if (getItemCount()-1<3){
           recyclerViewWeakReference.get().setLayoutManager(new GridLayoutManager(recyclerViewWeakReference.get().getContext(),2,GridLayoutManager.VERTICAL,false));
        }else if (getItemCount()-1<=9){
           recyclerViewWeakReference.get().setLayoutManager(new GridLayoutManager(recyclerViewWeakReference.get().getContext(),3,GridLayoutManager.VERTICAL,false));
        }else {
           recyclerViewWeakReference.get().setLayoutManager(new GridLayoutManager(recyclerViewWeakReference.get().getContext(),4,GridLayoutManager.VERTICAL,false));
        }
        int index = memberList.indexOf(data);
        if (index == -1) {
            return;
        }
        if (index >= memberList.size()) {
            return;
        }
        memberList.remove(index);
        notifyItemRemoved(index);
        compatibilityDataSizeChanged(0);
        notifyItemRangeChanged(index, this.memberList.size() - index);
    }

    protected void compatibilityDataSizeChanged(int size) {
        if (this.memberList.size() == size) {
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoCanvas canvas = memberList.get(position).getVideoCanvas(holder.itemView.getContext());
        if (canvas!=null){
            if (canvas.getView().getParent()!=null){
                ((ViewGroup)canvas.getView().getParent()).removeView(canvas.getView());
            }
        }

        if (memberList.get(position).getUid().equals(App.Companion.getApp().getUserId())){
            if (canvas!=null&&canvas.getView()!=null&&!canvas.getView().isAvailable()){
                memberList.get(position).setCanvas(new VideoCanvas(RtcEngine.CreateRendererView(holder.itemView.getContext())));
                rtcEngine.setupLocalVideo(memberList.get(position).getCanvas());
            }
        }

        GridLayoutManager layoutManager = (GridLayoutManager) recyclerViewWeakReference.get().getLayoutManager();
        float width = layoutManager.getWidth()/(layoutManager.getSpanCount()*1f);
        float height = width * 1.333333f;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int)width,(int)height);
        holder.getRoot().setLayoutParams(params);
        holder.getRoot().addView(memberList.get(position).getCanvas().getView());
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final RelativeLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.rl_root);
        }

        public RelativeLayout getRoot(){
            return root;
        }
    }
}
