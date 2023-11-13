package com.xlntsmmr.xlnt_timeline.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.xlntsmmr.xlnt_timeline.R;

public class ShimmerCalendarAdapter extends RecyclerView.Adapter<ShimmerCalendarAdapter.ViewHolder>{
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shimmer_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 30;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView mcv_status;
        Chip cgDay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mcv_status = itemView.findViewById(R.id.mcv_status);
            cgDay = itemView.findViewById(R.id.cg_day);
        }


    }
}
