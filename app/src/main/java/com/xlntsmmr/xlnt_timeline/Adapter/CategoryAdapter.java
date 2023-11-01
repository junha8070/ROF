package com.xlntsmmr.xlnt_timeline.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.xlntsmmr.xlnt_timeline.R;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    ArrayList<CategoryEntity> arr_categoryEntity;

    public CategoryAdapter(ArrayList<CategoryEntity> arr_categoryEntity, OnItemClickListener listener) {
        this.arr_categoryEntity = arr_categoryEntity;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryEntity categoryEntity = arr_categoryEntity.get(position);
        holder.chip_category.setText(categoryEntity.getTitle());

        if(position == arr_categoryEntity.size()-1){
            holder.chip_category.setClickable(true);
            holder.chip_category.setCheckable(false);
            holder.chip_category.setCheckedIconVisible(false);
        }

        holder.chip_category.setOnClickListener(v -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return arr_categoryEntity.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ChipGroup chip_group;
        Chip chip_category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // View 초기화 작업
            chip_group = itemView.findViewById(R.id.chip_group);
            chip_category = itemView.findViewById(R.id.chip_category);
        }
    }
}
