package com.xlntsmmr.xlnt_timeline.BottomSheetFragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.ViewModel.CategoryViewModel;
import com.xlntsmmr.xlnt_timeline.databinding.BottomSheetFragmentDialogAddBinding;

import java.util.List;

public class AddBottomSheetFragment extends BottomSheetDialogFragment {

    private BottomSheetFragmentDialogAddBinding binding;
    private CategoryViewModel categoryViewModel;

    public interface OnAddListener {
        void onAddListener(int which);
    }

    private OnAddListener mListener;

    int which = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = BottomSheetFragmentDialogAddBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), new Observer<List<CategoryEntity>>() {
            @Override
            public void onChanged(List<CategoryEntity> categoryEntities) {
                if(categoryEntities.size()<1){
                    binding.btnRof.setBackgroundColor(Color.GRAY);
                    Toast.makeText(getContext(), "카테고리를 먼저 추가해주세요.", Toast.LENGTH_SHORT).show();
                    binding.btnRof.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), "카테고리를 먼저 추가해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    binding.btnRof.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            which = 2;
                            dismiss();
                        }
                    });
                }
            }
        });

        binding.btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which = 1;
                dismiss();
            }
        });

        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        if(which!=0){
            if (mListener != null) {
                mListener.onAddListener(which);
            }
        }
    }

    public void setOnAddListener(OnAddListener mListener) {
        this.mListener = mListener;
    }
}