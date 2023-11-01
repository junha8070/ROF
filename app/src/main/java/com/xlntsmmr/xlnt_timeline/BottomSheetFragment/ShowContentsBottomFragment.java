package com.xlntsmmr.xlnt_timeline.BottomSheetFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.databinding.FragmentShowContentsBottomBinding;
import com.xlntsmmr.xlnt_timeline.Entity.TimeLineEntity;
import com.xlntsmmr.xlnt_timeline.ViewModel.CategoryViewModel;
import com.xlntsmmr.xlnt_timeline.ViewModel.TimeLineViewModel;

import java.util.Calendar;
import java.util.List;

public class ShowContentsBottomFragment extends BottomSheetDialogFragment {

    private FragmentShowContentsBottomBinding binding;
    private CategoryViewModel categoryViewModel;
    private TimeLineViewModel timeLineViewModel;

    private String UUID = "";

    private int check_category_id = 0;

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
        binding = FragmentShowContentsBottomBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        binding.yearPicker.setMaxValue(2100);
        binding.yearPicker.setMinValue(1900);

        binding.monthPicker.setMaxValue(12);
        binding.monthPicker.setMinValue(1);

        binding.dayPicker.setMaxValue(31);
        binding.dayPicker.setMinValue(1);

        timeLineViewModel.getTimeLineByUUID(UUID).observe(getViewLifecycleOwner(), new Observer<TimeLineEntity>() {
            @Override
            public void onChanged(TimeLineEntity timeLine) {
                if (timeLine != null) {
                    categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), new Observer<List<CategoryEntity>>() {
                        @Override
                        public void onChanged(List<CategoryEntity> categoryEntities) {


                            for(int i =0;i<categoryEntities.size();i++){
                                Chip chip = new Chip(getContext());
                                chip.setText(categoryEntities.get(i).getTitle());
                                chip.setCheckable(true);
                                binding.cgCategory.addView(chip);
                                if(categoryEntities.get(i).getTitle().equals(timeLine.getCategory())){
                                    check_category_id = chip.getId();
                                }

                            }

                            binding.edtContents.setText(timeLine.getContents());
                            binding.yearPicker.setValue(timeLine.getYear());
                            binding.monthPicker.setValue(timeLine.getMonth());
                            binding.dayPicker.setValue(timeLine.getDay());
                            binding.edtMemo.setText(timeLine.getMemo());
                            binding.cgCategory.check(check_category_id);
                        }
                    });

                } else {
                    // 해당 UUID에 대한 정보가 없는 경우 처리
                }
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
}