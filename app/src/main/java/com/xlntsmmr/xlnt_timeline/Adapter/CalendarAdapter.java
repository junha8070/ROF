package com.xlntsmmr.xlnt_timeline.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.xlntsmmr.xlnt_timeline.DTO.CalendarDayDTO;
import com.xlntsmmr.xlnt_timeline.R;
import com.xlntsmmr.xlnt_timeline.Entity.TimeLineEntity;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    String TAG = "CalendarAdapter";

    private List<CalendarDayDTO> calendarDayDTOS;
    ArrayList<TimeLineEntity> timeLineEntities;

    boolean no_item = false;
    boolean ready_item = false;
    boolean onGoing_item = false;
    boolean finish_item = false;

    public CalendarAdapter(List<CalendarDayDTO> calendarDayDTOS, ArrayList<TimeLineEntity> timeLineEntities) {
        this.calendarDayDTOS = calendarDayDTOS;
        this.timeLineEntities = timeLineEntities;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CalendarDayDTO calendarDayDTO = calendarDayDTOS.get(position);
        ready_item = false;
        onGoing_item = false;
        finish_item = false;

        if (calendarDayDTO != null) {
            holder.mcv_status.setVisibility(View.VISIBLE);
            holder.iv_status_top.setVisibility(View.VISIBLE);
            holder.iv_status_middle.setVisibility(View.VISIBLE);
            holder.iv_status_bottom.setVisibility(View.VISIBLE);
            holder.cgDay.setVisibility(View.VISIBLE);
            holder.cgDay.setText(String.valueOf(calendarDayDTO.getDayOfMonth()));
            holder.cgDay.setChecked(calendarDayDTO.isSelected());
            for (int i = 0; i < timeLineEntities.size(); i++) {
                if (calendarDayDTO.getYear() == timeLineEntities.get(i).getYear() &&
                        calendarDayDTO.getMonth() == timeLineEntities.get(i).getMonth() &&
                        calendarDayDTO.getDayOfMonth() == timeLineEntities.get(i).getDay()) {
//                    Log.d(TAG, calendarDay.getYear()+"년"+calendarDay.getMonth()+"월"+calendarDay.getDayOfMonth()+"일");
//                    Log.d(TAG, "Timeline status: "+timeLineEntities.get(i).getStatus());
                    if (timeLineEntities.get(i).getStatus() == 0) {
                        ready_item = true;
                    } else if (timeLineEntities.get(i).getStatus() == 1) {
                        onGoing_item = true;
                    } else if (timeLineEntities.get(i).getStatus() == 2) {
                        finish_item = true;
                    }
//                    Log.d(TAG, position+" "+i+" ready_item: " + ready_item);
//                    Log.d(TAG, position+" "+i+" onGoing_item: " + onGoing_item);
//                    Log.d(TAG, position+" "+i+" finish_item: " + finish_item);
                }
            }

            // 상태에 따라 이미지 설정
            if (ready_item && !onGoing_item && !finish_item) {
                holder.iv_status_top.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.ready_gray));
                holder.iv_status_middle.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.ready_gray));
                holder.iv_status_bottom.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.ready_gray));
            } else if (!ready_item && onGoing_item && !finish_item) {
                holder.iv_status_top.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.onGoing_green));
                holder.iv_status_middle.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.onGoing_green));
                holder.iv_status_bottom.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.onGoing_green));
            } else if (!ready_item && !onGoing_item && finish_item) {
                holder.iv_status_top.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.finish_blue));
                holder.iv_status_middle.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.finish_blue));
                holder.iv_status_bottom.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.finish_blue));
            } else if (ready_item && onGoing_item && !finish_item) {
                holder.iv_status_top.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.ready_gray));
                holder.iv_status_middle.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.onGoing_green));
                holder.iv_status_bottom.setVisibility(View.GONE);
            } else if (ready_item && !onGoing_item && finish_item) {
                holder.iv_status_top.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.ready_gray));
                holder.iv_status_middle.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.finish_blue));
                holder.iv_status_bottom.setVisibility(View.GONE);
            } else if (!ready_item && onGoing_item && finish_item) {
                holder.iv_status_top.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.onGoing_green));
                holder.iv_status_middle.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.finish_blue));
                holder.iv_status_bottom.setVisibility(View.GONE);
            } else if (ready_item && onGoing_item && finish_item) {
                holder.iv_status_top.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.ready_gray));
                holder.iv_status_middle.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.onGoing_green));
                holder.iv_status_bottom.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.finish_blue));
            } else {
                holder.mcv_status.setCardBackgroundColor(0);
                holder.iv_status_top.setAlpha(0.5f);
                holder.iv_status_middle.setAlpha(0.5f);
                holder.iv_status_bottom.setAlpha(0.5f);
                holder.iv_status_top.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.background_gray));
                holder.iv_status_middle.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.background_gray));
                holder.iv_status_bottom.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.background_gray));
            }
        } else {
            holder.mcv_status.setVisibility(View.GONE);
            holder.iv_status_top.setVisibility(View.GONE);
            holder.iv_status_middle.setVisibility(View.GONE);
            holder.iv_status_bottom.setVisibility(View.GONE);
            holder.cgDay.setVisibility(View.GONE);
        }




//        Log.d(TAG, "position: "+position);
        holder.cgDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, String.valueOf(position));
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

//        Log.d(TAG, position+"position ready_item: " + ready_item);
//        Log.d(TAG, position+"position onGoing_item: " + onGoing_item);
//        Log.d(TAG, position+"position finish_item: " + finish_item);
//        Log.d(TAG, position+"position getColorFilter: " + holder.ivStatus.getColorFilter());

    }

    @Override
    public int getItemCount() {
        return calendarDayDTOS.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView mcv_status, iv_status_top, iv_status_middle, iv_status_bottom;
        Chip cgDay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mcv_status = itemView.findViewById(R.id.mcv_status);
            iv_status_top = itemView.findViewById(R.id.iv_status_top);
            iv_status_middle = itemView.findViewById(R.id.iv_status_middle);
            iv_status_bottom = itemView.findViewById(R.id.iv_status_bottom);
            cgDay = itemView.findViewById(R.id.cg_day);
        }


    }
}
