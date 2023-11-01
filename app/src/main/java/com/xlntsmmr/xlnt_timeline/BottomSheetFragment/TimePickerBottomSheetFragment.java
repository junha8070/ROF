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

public class TimePickerBottomSheetFragment extends BottomSheetDialogFragment {


    private NumberPicker hourPicker, minutePicker;

    public interface OnTimeSelectedListener {
        void OnTimeSelectedListener(String formattedDate);
    }

    private OnTimeSelectedListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time_bottom_sheet, container, false);

        hourPicker = view.findViewById(R.id.hour_picker);
        minutePicker = view.findViewById(R.id.minute_picker);

        hourPicker.setMaxValue(23);
        hourPicker.setMinValue(0);

        minutePicker.setMaxValue(59);
        minutePicker.setMinValue(0);

        return view;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        int hour = hourPicker.getValue();
        int minute = minutePicker.getValue();

        String formattedDate = String.format("%02d시 %02d분", hour, minute);

        if (mListener != null) {
            mListener.OnTimeSelectedListener(formattedDate);
        }
    }

    public void setOnTimeSelectedListener(OnTimeSelectedListener listener) {
        mListener = listener;
    }

}
