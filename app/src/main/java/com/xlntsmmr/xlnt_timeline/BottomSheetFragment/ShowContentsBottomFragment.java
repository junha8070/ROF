package com.xlntsmmr.xlnt_timeline.BottomSheetFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.R;
import com.xlntsmmr.xlnt_timeline.Entity.TimeLineEntity;
import com.xlntsmmr.xlnt_timeline.ViewModel.CategoryViewModel;
import com.xlntsmmr.xlnt_timeline.ViewModel.TimeLineViewModel;
import com.xlntsmmr.xlnt_timeline.databinding.BottomSheetFragmentDialogEditCategoryBinding;
import com.xlntsmmr.xlnt_timeline.databinding.BottomSheetFragmentDialogEditRofBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShowContentsBottomFragment extends BottomSheetDialogFragment implements DatePickerBottomSheetFragment.OnDateSelectedListener {

    String TAG = "ShowContentsBottomFragment";

    private BottomSheetFragmentDialogEditRofBinding binding;
    private CategoryViewModel categoryViewModel;
    private TimeLineViewModel timeLineViewModel;

    private String UUID = "";

    private int check_category_id = 0;

    int select_year = 0;
    int select_month = 0;
    int select_day = 0;

    int currentNightMode;
    boolean isNightMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        timeLineViewModel = new ViewModelProvider(this).get(TimeLineViewModel.class);
    }

    public ShowContentsBottomFragment(String UUID) {
        this.UUID = UUID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = BottomSheetFragmentDialogEditRofBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        isNightMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES;

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

        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        timeLineViewModel.getTimeLineByUUID(UUID).observe(getViewLifecycleOwner(), new Observer<TimeLineEntity>() {
            @Override
            public void onChanged(TimeLineEntity timeLine) {
                if (timeLine != null) {
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
                                Log.d(TAG, "chip id: " + chip.getId());
                                Log.d(TAG, "chip title: " + categoryEntities.get(i).getTitle());
                                Log.d(TAG, "chip timeline category: " + timeLine.getCategory());
                                if (arr_category.get(i).getTitle().equals(timeLine.getCategory())) {
                                    check_category_id = chip.getId();
                                    Log.d(TAG, "chip id: " + chip.getId());
                                }

                            }

                            select_year = timeLine.getYear();
                            select_month = timeLine.getMonth();
                            select_day = timeLine.getDay();

                            binding.edtContents.setText(timeLine.getContents());
                            binding.edtDate.setText(String.format("%04d년 %02d월 %02d일", select_year, select_month, select_day));
                            binding.edtMemo.setText(timeLine.getMemo());
                            binding.cgCategory.check(check_category_id);
                        }
                    });

                } else {
                    // 해당 UUID에 대한 정보가 없는 경우 처리
                }
            }
        });

        binding.edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerBottomSheetFragment datePickerBottomSheetFragment = new DatePickerBottomSheetFragment(select_year, select_month, select_day);
                datePickerBottomSheetFragment.setOnDateSelectedListener(ShowContentsBottomFragment.this);
                datePickerBottomSheetFragment.show(getParentFragmentManager(), "DatePickerBottomSheetFragment");
            }
        });

        binding.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("삭제할까요?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 사용자가 확인을 눌렀을 때 수행할 작업
                                deleteTimeline(UUID);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 사용자가 취소를 눌렀을 때 수행할 작업
                                dialog.dismiss();
                            }
                        });
                // Create the AlertDialog object and return it
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contents = binding.edtContents.getText().toString();

                if (contents.isEmpty()||contents.trim().isEmpty()) {
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
                String category_uuid = categoryViewModel.getAllCategories().getValue().get(position).getUuid();
                String category = categoryViewModel.getAllCategories().getValue().get(position).getTitle();

                // 기존의 타임라인을 불러와서 수정
                timeLineViewModel.getTimeLineByUUID(UUID).observe(getViewLifecycleOwner(), new Observer<TimeLineEntity>() {
                    @Override
                    public void onChanged(TimeLineEntity timeLine) {
                        if (timeLine != null) {
                            Calendar calendar = Calendar.getInstance();
                            int currentYear = calendar.get(Calendar.YEAR);
                            int currentMonth = calendar.get(Calendar.MONTH) + 1;
                            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                            int currentHour = calendar.get(Calendar.HOUR);
                            int currentMinute = calendar.get(Calendar.MINUTE);
                            int currnetSecond = calendar.get(Calendar.SECOND);

                            TimeLineEntity updatedTimeLine = new TimeLineEntity(
                                    timeLine.getUuid(), category_uuid, category,
                                    contents, memo, timeLine.getStatus(),
                                    year, month, day,
                                    currentYear, currentMonth, currentDay,
                                    currentHour, currentMinute, currnetSecond,
                                    0, false, 0, 0);

                            // 업데이트된 타임라인을 저장
                            timeLineViewModel.updateTimeLine(updatedTimeLine);
                            dismiss();
                        }
                    }
                });
            }
        });


        return rootView;
    }

    private void deleteTimeline(String uuid) {
        timeLineViewModel.deleteTimeLineByUUID(uuid);
        dismiss();
    }

    @Override
    public void onDateSelected(String formattedDate) {
        binding.edtDate.setText(formattedDate);
    }
}