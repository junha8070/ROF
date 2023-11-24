package com.xlntsmmr.xlnt_timeline.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.R;

import java.util.ArrayList;

public class CategoryListMoveAdapter extends RecyclerView.Adapter<CategoryListMoveAdapter.ViewHolder>{

    private String TAG = "CategoryListMoveAdapter";

    public interface OnItemChangeListener {
        void setPosition(ArrayList<CategoryEntity> change_position_arr_category);
    }

    private OnItemChangeListener onItemChangeListener;

    public void setOnItemChangeListener(OnItemChangeListener listener) {
        this.onItemChangeListener = listener;
    }

    public interface OnItemClickListener {
        void clickListener(String CategoryUUID, String title);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    private ArrayList<CategoryEntity> arr_category;

    public CategoryListMoveAdapter(ArrayList<CategoryEntity> arr_category) {
        this.arr_category = arr_category;
    }

    @NonNull
    @Override
    public CategoryListMoveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_move_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListMoveAdapter.ViewHolder holder, int position) {
        CategoryEntity categoryEntity = arr_category.get(position);
        Log.d(TAG, position+" : "+categoryEntity.getTitle());
        holder.tv_title.setText(categoryEntity.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.clickListener(categoryEntity.getUuid(), categoryEntity.getTitle());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, ""+arr_category.size());
        return arr_category.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView tv_title;
        MaterialButton btn_move;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            btn_move = itemView.findViewById(R.id.btn_move);
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
        CategoryEntity fromCategory = arr_category.get(fromPosition);
        arr_category.remove(fromPosition);
        arr_category.add(toPosition, fromCategory);
        notifyItemMoved(fromPosition, toPosition);
        if (onItemChangeListener != null) {
            onItemChangeListener.setPosition(arr_category);
        }
    }

}
