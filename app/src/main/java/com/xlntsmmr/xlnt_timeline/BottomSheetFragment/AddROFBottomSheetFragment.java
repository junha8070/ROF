package com.xlntsmmr.xlnt_timeline.BottomSheetFragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.databinding.FragmentAddROFBottomSheetBinding;
import com.xlntsmmr.xlnt_timeline.Entity.TimeLineEntity;
import com.xlntsmmr.xlnt_timeline.ViewModel.CategoryViewModel;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class AddROFBottomSheetFragment extends BottomSheetDialogFragment {

    String TAG = "AddROFBottomSheetFragment";

    private FragmentAddROFBottomSheetBinding binding;
    private CategoryViewModel categoryViewModel;

    int select_year = 0;
    int select_month = 0;
    int select_day = 0;

    public interface OnROFDataListener {
        void onROFDataListener(TimeLineEntity timeLine);
    }

    public AddROFBottomSheetFragment() {
    }

    public AddROFBottomSheetFragment(int select_year, int select_month, int select_day) {
        this.select_year = select_year;
        this.select_month = select_month;
        this.select_day = select_day;
        Log.d(TAG, ""+select_year+" "+select_month+" "+select_day);
    }

    private OnROFDataListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddROFBottomSheetBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        binding.yearPicker.setMaxValue(2100);
        binding.yearPicker.setMinValue(1900);

        binding.monthPicker.setMaxValue(12);
        binding.monthPicker.setMinValue(1);

        binding.dayPicker.setMaxValue(31);
        binding.dayPicker.setMinValue(1);

        if(select_year!=0&&select_month!=0&&select_day!=0){
            binding.yearPicker.setValue(select_year);
            binding.monthPicker.setValue(select_month);
            binding.dayPicker.setValue(select_day);
        }else{
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 +1 해줍니다.
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

            // NumberPicker에 현재 날짜를 설정합니다.
            binding.yearPicker.setValue(currentYear);
            binding.monthPicker.setValue(currentMonth);
            binding.dayPicker.setValue(currentDay);
        }

        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), new Observer<List<CategoryEntity>>() {
            @Override
            public void onChanged(List<CategoryEntity> categoryEntities) {
                for(int i =0;i<categoryEntities.size();i++){
                    Chip chip = new Chip(getContext());
                    chip.setText(categoryEntities.get(i).getTitle());
                    chip.setCheckable(true);
                    binding.cgCategory.addView(chip);
                }
            }
        });

        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contents = binding.edtContents.getText().toString();

                if(contents.isEmpty()){
                    binding.edtContents.setError("내용을 입력해주세요.");
                    return;
                }

                int year = binding.yearPicker.getValue();
                int month = binding.monthPicker.getValue();
                int day = binding.dayPicker.getValue();

                String memo = binding.edtMemo.getText().toString();

                if(binding.cgCategory.getCheckedChipIds().size()<1){
                    binding.tvRequireSelect.setVisibility(View.VISIBLE);
                    return;
                }

                Chip selectedChip = binding.cgCategory.findViewById(binding.cgCategory.getCheckedChipIds().get(0));
                int position = binding.cgCategory.indexOfChild(selectedChip);
                String category_uuid = categoryViewModel.getAllCategories().getValue().get(position).getUuid();
                String category = categoryViewModel.getAllCategories().getValue().get(position).getTitle();

                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 +1 해줍니다.
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                int currentHour = calendar.get(Calendar.HOUR);
                int currentMinute = calendar.get(Calendar.MINUTE);
                int currnetSecond = calendar.get(Calendar.SECOND);

                TimeLineEntity timeLine = new TimeLineEntity(
                        UUID.randomUUID().toString(), category_uuid, category,
                        contents, memo,0,
                        year, month, day,
                        currentYear, currentMonth, currentDay,
                        currentHour, currentMinute,currnetSecond,
                        0,
                        false, 0, 0);

                if (mListener != null) {
                    mListener.onROFDataListener(timeLine);
                }

                dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void setOnROFListener(OnROFDataListener mListener) {
        this.mListener = mListener;
    }
}