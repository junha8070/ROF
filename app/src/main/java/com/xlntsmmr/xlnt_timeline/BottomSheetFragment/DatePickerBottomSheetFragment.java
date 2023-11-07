package com.xlntsmmr.xlnt_timeline.BottomSheetFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.xlntsmmr.xlnt_timeline.R;
import com.xlntsmmr.xlnt_timeline.databinding.BottomSheetFragmentDialogAddCategoryBinding;
import com.xlntsmmr.xlnt_timeline.databinding.BottomSheetFragmentDialogSetCalendarBinding;


import android.widget.NumberPicker;

import java.util.Calendar;

public class DatePickerBottomSheetFragment extends BottomSheetDialogFragment {

    String TAG = "DatePickerBottomSheetFragment";

    private BottomSheetFragmentDialogSetCalendarBinding binding;

    int select_year = 0;
    int select_month = 0;
    int select_day = 0;

    public interface OnDateSelectedListener {
        void onDateSelected(String formattedDate);
    }

    public DatePickerBottomSheetFragment(int select_year, int select_month, int select_day) {
        this.select_year = select_year;
        this.select_month = select_month;
        this.select_day = select_day;
        Log.d(TAG, ""+select_year+" "+select_month+" "+select_day);
    }

    private OnDateSelectedListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = BottomSheetFragmentDialogSetCalendarBinding.inflate(inflater, container, false);
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

        return rootView;
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        int year = binding.yearPicker.getValue();
        int month = binding.monthPicker.getValue();
        int day = binding.dayPicker.getValue();

        String formattedDate = String.format("%04d년 %02d월 %02d일", year, month, day);

        if (mListener != null) {
            mListener.onDateSelected(formattedDate);
        }
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        mListener = listener;
    }
}
