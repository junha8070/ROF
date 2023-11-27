package com.xlntsmmr.xlnt_timeline.BottomSheetFragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.Entity.TimeLineEntity;
import com.xlntsmmr.xlnt_timeline.R;
import com.xlntsmmr.xlnt_timeline.ViewModel.CategoryViewModel;
import com.xlntsmmr.xlnt_timeline.databinding.BottomSheetFragmentDialogAddRofBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class AddROFBottomSheetFragment extends BottomSheetDialogFragment implements DatePickerBottomSheetFragment.OnDateSelectedListener {

    String TAG = "AddROFBottomSheetFragment";

    int currentNightMode;
    boolean isNightMode;

    private BottomSheetFragmentDialogAddRofBinding binding;
    private CategoryViewModel categoryViewModel;

    int select_year = 0;
    int select_month = 0;
    int select_day = 0;

    @Override
    public void onDateSelected(String formattedDate) {
        binding.edtDate.setText(formattedDate);
    }

    public interface OnROFDataListener {
        void onROFDataListener(TimeLineEntity timeLine);
    }

    public AddROFBottomSheetFragment() {
    }

    public AddROFBottomSheetFragment(int select_year, int select_month, int select_day) {
        this.select_year = select_year;
        this.select_month = select_month;
        this.select_day = select_day;
        Log.d(TAG, "" + select_year + " " + select_month + " " + select_day);
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
        binding = BottomSheetFragmentDialogAddRofBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        isNightMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES;

//        setStyle(STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        binding.edtContents.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                binding.edtMemo.requestFocus();
                return true;
            }
        });

        binding.edtMemo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.edtMemo.getWindowToken(), 0);    //hide keyboard
                    return true;
                }
                return false;
            }
        });


        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), new Observer<List<CategoryEntity>>() {
            @Override
            public void onChanged(List<CategoryEntity> categoryEntities) {

                ArrayList<CategoryEntity> arr_category = new ArrayList<>();
                arr_category.addAll(categoryEntities);

                Collections.sort(arr_category, new Comparator<CategoryEntity>() {
                    @Override
                    public int compare(CategoryEntity category1, CategoryEntity category2) {
                        return Integer.compare(category1.getPosition(), category2.getPosition());
                    }
                });

                for (int i = 0; i < arr_category.size(); i++) {
                    Chip chip = new Chip(getContext());
                    chip.setText(arr_category.get(i).getTitle());
                    chip.setCheckable(true);
                    chip.setChipStrokeWidth(3);

                    if (isNightMode) {
                        chip.setChipBackgroundColorResource(R.color.bottomSheetBackground_dark);
                    } else {
                        chip.setChipBackgroundColorResource(R.color.light_background);
                    }

                    chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                chip.setChipBackgroundColorResource(R.color.main_orange);
                                chip.setChipStrokeWidth(0);
                                chip.setTextColor(getResources().getColor(R.color.white, getContext().getTheme()));
                            } else {
                                if (isNightMode) {
                                    chip.setChipBackgroundColorResource(R.color.bottomSheetBackground_dark);
                                    chip.setChipStrokeWidth(3);
                                    chip.setTextColor(getResources().getColor(R.color.white, getContext().getTheme()));
                                } else {
                                    chip.setChipBackgroundColorResource(R.color.light_background);
                                    chip.setChipStrokeWidth(3);
                                    chip.setTextColor(getResources().getColor(R.color.black, getContext().getTheme()));
                                }
                            }
                        }
                    });
                    binding.cgCategory.addView(chip);
                }
            }
        });

        binding.edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerBottomSheetFragment datePickerBottomSheetFragment = new DatePickerBottomSheetFragment(select_year, select_month, select_day);
                datePickerBottomSheetFragment.setOnDateSelectedListener(AddROFBottomSheetFragment.this);
                datePickerBottomSheetFragment.show(getParentFragmentManager(), "DatePickerBottomSheetFragment");
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

                if(binding.edtDate.getText().toString().isEmpty()){
                    binding.edtDate.setError("날짜를 선택해주세요.");
                    return;
                }

                if (contents.isEmpty() || contents.trim().isEmpty()) {
                    binding.edtContents.setError("내용을 입력해주세요.");
                    return;
                }

                int year = Integer.parseInt((String) binding.edtDate.getText().toString().substring(0, 4));
                int month = Integer.parseInt((String) binding.edtDate.getText().toString().substring(6, 8));
                int day = Integer.parseInt((String) binding.edtDate.getText().toString().substring(10, 12));

                String memo = binding.edtMemo.getText().toString();

                if (binding.cgCategory.getCheckedChipIds().size() < 1) {
                    binding.tvRequireSelect.setVisibility(View.VISIBLE);
                    return;
                }

                Chip selectedChip = binding.cgCategory.findViewById(binding.cgCategory.getCheckedChipIds().get(0));
                int position = binding.cgCategory.indexOfChild(selectedChip);
                Log.d(TAG, "binding.cgCategory: " + position);

                ArrayList<CategoryEntity> categoryEntities = new ArrayList<>();
                categoryEntities.addAll(categoryViewModel.getAllCategories().getValue());

                Collections.sort(categoryEntities, new Comparator<CategoryEntity>() {
                    @Override
                    public int compare(CategoryEntity category1, CategoryEntity category2) {
                        return Integer.compare(category1.getPosition(), category2.getPosition());
                    }
                });

                String category_uuid = categoryEntities.get(position).getUuid();
                String category = categoryEntities.get(position).getTitle();

                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 +1 해줍니다.
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                int currentHour = calendar.get(Calendar.HOUR);
                int currentMinute = calendar.get(Calendar.MINUTE);
                int currnetSecond = calendar.get(Calendar.SECOND);

                TimeLineEntity timeLine = new TimeLineEntity(
                        UUID.randomUUID().toString(), category_uuid, category,
                        contents, memo, 0,
                        year, month, day,
                        currentYear, currentMonth, currentDay,
                        currentHour, currentMinute, currnetSecond,
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