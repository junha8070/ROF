package com.xlntsmmr.xlnt_timeline.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.xlntsmmr.xlnt_timeline.DTO.ContentDTO;
import com.xlntsmmr.xlnt_timeline.R;
import com.xlntsmmr.xlnt_timeline.DTO.TimeLineDTO;

import java.util.ArrayList;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder>{

    String TAG = "ContentAdapter";

    ArrayList<TimeLineDTO> arr_timeline;

    public interface OnStatusButtonClickListener {
        void onStatusButtonClick(String uuid, int status);
    }

    public interface OnEdtAndMoreButtonClickListener{
        void onEdtAndMoreButtonClickListener(String UUID);
    }

    private OnStatusButtonClickListener listener;
    private OnEdtAndMoreButtonClickListener onEdtAndMoreButtonClickListener;

    public void setOnStatusButtonClickListener(OnStatusButtonClickListener listener) {
        this.listener = listener;
    }

    public void setOnEdtAndMoreButtonClickListener(OnEdtAndMoreButtonClickListener onEdtAndMoreButtonClickListener){
        this.onEdtAndMoreButtonClickListener = onEdtAndMoreButtonClickListener;
    }


    public ContentAdapter(ArrayList<TimeLineDTO> arr_timeline) {
        this.arr_timeline = arr_timeline;
    }

    @NonNull
    @Override
    public ContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "position: "+position);
        ContentDTO contentDTO = arr_timeline.get(position).getContentDTO();
        switch (contentDTO.getStatus()){
            case 0:
                holder.btn_status.setIconTintResource(R.color.ready_gray);
                break;
            case 1:
                holder.btn_status.setIconTintResource(R.color.onGoing_green);
                break;
            case 2:
                holder.btn_status.setIconTintResource(R.color.finish_blue);
                break;
            default:
                holder.btn_status.setIconTintResource(R.color.ready_gray);
                break;
        }

        holder.edt_contents.setText(contentDTO.getContent());
        Log.d(TAG, contentDTO.getContent());

        holder.btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "ContentAdapter_btn_status clicked");
                if (listener != null) {
                    Log.d(TAG, "ContentAdapter_btn_status listener worked");
                    listener.onStatusButtonClick(arr_timeline.get(position).getUUID(), contentDTO.getStatus());
                }
            }
        });

        holder.btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onEdtAndMoreButtonClickListener!=null){
                    onEdtAndMoreButtonClickListener.onEdtAndMoreButtonClickListener(arr_timeline.get(position).getUUID());
                }
            }
        });

        holder.edt_contents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onEdtAndMoreButtonClickListener!=null){
                    onEdtAndMoreButtonClickListener.onEdtAndMoreButtonClickListener(arr_timeline.get(position).getUUID());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, String.valueOf("사이즈"+arr_timeline.size()));
        return arr_timeline.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MaterialButton btn_status, btn_more;
        TextInputEditText edt_contents;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // View 초기화 작업
            btn_status = itemView.findViewById(R.id.btn_status);
            edt_contents = itemView.findViewById(R.id.edt_contents);
            btn_more = itemView.findViewById(R.id.btn_more);
        }
    }
}
