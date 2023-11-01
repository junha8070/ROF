package com.xlntsmmr.xlnt_timeline.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.CategoryBottomSheetFragment;
import com.xlntsmmr.xlnt_timeline.Fragment.HomeFragment;
import com.xlntsmmr.xlnt_timeline.R;
import com.xlntsmmr.xlnt_timeline.DTO.TimeLineDTO;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder> {

    public interface OnStatusButtonClickListener {
        void onStatusButtonClick(String uuid, int status);
    }
    public interface OnEdtAndMoreButtonClickListener{
        void onEdtAndMoreButtonClickListener(String UUID);
    }
    public interface OnCategoryChipLongClickListener{
        void onCategoryChipLongClickListener(String category_UUID, String title);
    }

    private OnStatusButtonClickListener listener;
    private ContentAdapter.OnEdtAndMoreButtonClickListener onEdtAndMoreButtonClickListener;
    private OnCategoryChipLongClickListener onCategoryChipLongClickListener;

    public void setOnStatusButtonClickListener(OnStatusButtonClickListener listener) {
        this.listener = listener;
    }
    public void setOnEdtAndMoreButtonClickListener(ContentAdapter.OnEdtAndMoreButtonClickListener onEdtAndMoreButtonClickListener){
        this.onEdtAndMoreButtonClickListener = onEdtAndMoreButtonClickListener;
    }

    public void setOnCategoryChipLongClickListener(OnCategoryChipLongClickListener onCategoryChipLongClickListener) {
        this.onCategoryChipLongClickListener = onCategoryChipLongClickListener;
    }

    String TAG = "TimeLineAdapter";

    //    ArrayList<TimeLineDTO> arr_timeline;
//    ArrayList<CategoryEntity> arr_category;
    HashMap<CategoryEntity, ArrayList<TimeLineDTO>> arr_ROF;
    ContentAdapter contentAdapter;
    private float initialX;
    private float initialY;

    public TimeLineAdapter(HashMap<CategoryEntity, ArrayList<TimeLineDTO>> arr_ROF) {
        this.arr_ROF = arr_ROF;
    }

    @NonNull
    @Override
    public TimeLineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineAdapter.ViewHolder holder, int position) {
        ArrayList<CategoryEntity> categoryEntities = new ArrayList<>(arr_ROF.keySet());
        CategoryEntity categoryEntity = categoryEntities.get(position);

        // CategoryEntity의 title을 tv_title에 설정
        holder.chip_title.setText(categoryEntity.getTitle());

        ArrayList<TimeLineDTO> timeLineDTOs = new ArrayList<>();
        timeLineDTOs.addAll(arr_ROF.get(categoryEntity));

//        Log.d(TAG, String.valueOf(timeLineDTOs.get(2).contentDTO.getContent()));

        // contentAdapter의 RecyclerView를 LinearLayoutManager로 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false);
        holder.rv_content.setLayoutManager(layoutManager);

        // contentAdapter를 초기화하고 TimeLineDTO 리스트를 전달
        ContentAdapter contentAdapter = new ContentAdapter(timeLineDTOs);
        holder.rv_content.setAdapter(contentAdapter);

        contentAdapter.setOnStatusButtonClickListener(new ContentAdapter.OnStatusButtonClickListener() {
            @Override
            public void onStatusButtonClick(String uuid, int status) {
                Log.d(TAG, "contentAdapter uuid: "+uuid);
                Log.d(TAG, "contentAdapter position: "+status);
                listener.onStatusButtonClick(uuid, status);
            }
        });

        contentAdapter.setOnEdtAndMoreButtonClickListener(new ContentAdapter.OnEdtAndMoreButtonClickListener() {
            @Override
            public void onEdtAndMoreButtonClickListener(String UUID) {
                onEdtAndMoreButtonClickListener.onEdtAndMoreButtonClickListener(UUID);
            }
        });

        holder.chip_title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onCategoryChipLongClickListener.onCategoryChipLongClickListener(categoryEntity.getUuid(), categoryEntity.getTitle());
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return arr_ROF.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        Chip chip_title;
        RecyclerView rv_content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // View 초기화 작업
//            view = itemView.findViewById(R.id.view);
//            iv_indicator = itemView.findViewById(R.id.iv_indicator);
//            tv_day = itemView.findViewById(R.id.tv_day);
//            rv_content = itemView.findViewById(R.id.rv_content);
            chip_title = itemView.findViewById(R.id.tv_title);
            rv_content = itemView.findViewById(R.id.rv_content);
        }
    }

//    @Override
//    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
//        super.onViewAttachedToWindow(holder);
//
//        int position = holder.getAdapterPosition();
//        if (position == arr_timeline.size() - 1) {
//            // 마지막 아이템에 대한 패딩 설정
//            holder.rv_content.setPadding(0, 0, 0, 0); // 원하는 패딩 값을 설정하세요
//        } else {
//            // 다른 아이템에 대한 패딩 초기화
//            holder.rv_content.setPadding(0, 0, 0, 0);
//        }
//    }

}
