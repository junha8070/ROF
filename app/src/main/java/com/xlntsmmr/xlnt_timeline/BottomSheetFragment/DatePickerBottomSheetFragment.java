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


import android.widget.NumberPicker;

import java.util.Calendar;

public class DatePickerBottomSheetFragment extends BottomSheetDialogFragment {

    String TAG = "DatePickerBottomSheetFragment";
    private NumberPicker yearPicker, monthPicker, dayPicker;

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
        View view = inflater.inflate(R.layout.calendar_bottom_sheet, container, false);

        yearPicker = view.findViewById(R.id.year_picker);
        monthPicker = view.findViewById(R.id.month_picker);
        dayPicker = view.findViewById(R.id.day_picker);


        yearPicker.setMaxValue(2100);
        yearPicker.setMinValue(1900);

        monthPicker.setMaxValue(12);
        monthPicker.setMinValue(1);

        dayPicker.setMaxValue(31);
        dayPicker.setMinValue(1);

        if(select_year!=0&&select_month!=0&&select_day!=0){
            yearPicker.setValue(select_year);
            monthPicker.setValue(select_month);
            dayPicker.setValue(select_day);
        }else{
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 +1 해줍니다.
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

            // NumberPicker에 현재 날짜를 설정합니다.
            yearPicker.setValue(currentYear);
            monthPicker.setValue(currentMonth);
            dayPicker.setValue(currentDay);
        }

        return view;
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        int year = yearPicker.getValue();
        int month = monthPicker.getValue();
        int day = dayPicker.getValue();

        String formattedDate = String.format("%04d년 %02d월 %02d일", year, month, day);

        if (mListener != null) {
            mListener.onDateSelected(formattedDate);
        }
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        mListener = listener;
    }
}
