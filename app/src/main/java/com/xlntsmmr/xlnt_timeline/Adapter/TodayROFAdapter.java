package com.xlntsmmr.xlnt_timeline.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.xlntsmmr.xlnt_timeline.Entity.TimeLineEntity;
import com.xlntsmmr.xlnt_timeline.R;

import java.util.ArrayList;
import java.util.List;

public class TodayROFAdapter extends RecyclerView.Adapter<TodayROFAdapter.ViewHolder>{

    String TAG = "TodayROFAdapter";

    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;
    private List<TimeLineEntity> data = new ArrayList<>();


    public interface OnItemClickListener {
        void onItemClick(String uuid, int status);
    }

    public interface OnItemLongClickListener {
        void onItemLongClickListener(String uuid);
    }

//    public TodayROFAdapter(List<TimeLineEntity> data) {
//        this.data = data;
//    }
//
//    public void updateData(List<TimeLineEntity> newData) {
//        data.clear();
//        data.addAll(newData);
//        notifyDataSetChanged();
//    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setLongClickListener(OnItemLongClickListener listener){this.longClickListener = listener;}

    ArrayList<TimeLineEntity> arr_today_timeline;

    public TodayROFAdapter(ArrayList<TimeLineEntity> arr_today_timeline) {
        this.arr_today_timeline = arr_today_timeline;
//        Log.d(TAG, arr_today_timeline.get(0).getContents());
    }

    @NonNull
    @Override
    public TodayROFAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_today_rof, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayROFAdapter.ViewHolder holder, int position) {

        TimeLineEntity timeLine = arr_today_timeline.get(position);

        holder.tv_title.setText(timeLine.getContents());
        holder.tv_category.setText(timeLine.getCategory());
        holder.tv_memo.setText(timeLine.getMemo());
        switch (timeLine.getStatus()){
            case 0:
                holder.mcv_timeline.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.ready_gray));
//                holder.tv_status.setText("Ready");
                break;
            case 1:
                holder.mcv_timeline.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.onGoing_green));
//                holder.tv_status.setText("On Going");
                break;
            case 2:
                holder.mcv_timeline.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.finish_blue));
//                holder.tv_status.setText("Finish");
                break;
            default:
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(timeLine.getUuid(), timeLine.getStatus());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClickListener.onItemLongClickListener(timeLine.getUuid());
                return true;
            }
        });

    }

    public void updateStatus(int position, int newStatus) {
        arr_today_timeline.get(position).setStatus(newStatus);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return arr_today_timeline.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView mcv_timeline;
        MaterialTextView tv_title, tv_category, tv_memo, tv_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mcv_timeline = itemView.findViewById(R.id.mcv_timeline);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_category = itemView.findViewById(R.id.tv_category);
            tv_memo = itemView.findViewById(R.id.tv_memo);
//            tv_status = itemView.findViewById(R.id.tv_status);
        }


    }
}
