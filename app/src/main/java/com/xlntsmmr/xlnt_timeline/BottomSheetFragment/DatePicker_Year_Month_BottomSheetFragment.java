package com.xlntsmmr.xlnt_timeline.BottomSheetFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.xlntsmmr.xlnt_timeline.R;

import java.util.Calendar;

public class DatePicker_Year_Month_BottomSheetFragment extends BottomSheetDialogFragment {

    private NumberPicker yearPicker, monthPicker, dayPicker;

    public interface On_Year_Month_SelectedListener {
        void on_Year_Month_Selected(int year, int month, String category_name);
    }

    private On_Year_Month_SelectedListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_year_month_bottom_sheet, container, false);

        yearPicker = view.findViewById(R.id.year_picker);
        monthPicker = view.findViewById(R.id.month_picker);

        // 현재 날짜를 얻어옵니다.
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 +1 해줍니다.

        // NumberPicker에 현재 날짜를 설정합니다.
        yearPicker.setMaxValue(2100);
        yearPicker.setMinValue(1900);
        yearPicker.setValue(currentYear);

        monthPicker.setMaxValue(12);
        monthPicker.setMinValue(1);
        monthPicker.setValue(currentMonth);

        return view;
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        int year = yearPicker.getValue();
        int month = monthPicker.getValue();

        String formattedDate = String.format("%d월", month);

        if (mListener != null) {
            mListener.on_Year_Month_Selected(year, month, formattedDate);
        }
    }

    public void setOnDateSelectedListener(On_Year_Month_SelectedListener listener) {
        mListener = listener;
    }
}
