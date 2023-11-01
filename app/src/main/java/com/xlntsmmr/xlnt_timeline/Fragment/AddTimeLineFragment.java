//package com.xlntsmmr.xlnt_timeline;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.navigation.Navigation;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CompoundButton;
//import android.widget.Toast;
//
//import com.google.android.material.chip.Chip;
//import com.google.android.material.chip.ChipGroup;
//import com.google.android.material.slider.Slider;
//import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
//import com.xlntsmmr.xlnt_timeline.databinding.FragmentAddTimeLineBinding;
//import com.xlntsmmr.xlnt_timeline.Entity.TimeLineEntity;
//import com.xlntsmmr.xlnt_timeline.viewmodel.TimeLineViewModel;
//import com.xlntsmmr.xlnt_timeline.viewmodel.CategoryViewModel;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.UUID;
//
//public class AddTimeLineFragment extends Fragment implements DatePickerBottomSheetFragment.OnDateSelectedListener, TimePickerBottomSheetFragment.OnTimeSelectedListener {
//
//    private FragmentAddTimeLineBinding binding;
//    private SimpleDateFormat dateFormat;
//    private CategoryViewModel categoryViewModel;
//    private TimeLineViewModel timeLineViewModel;
//    ArrayList<CategoryEntity> categoryEntities;
//    CategoryAdapter categoryAdapter;
//    String category = "";
//    String category_uuid = "";
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
//        timeLineViewModel = new ViewModelProvider(this).get(TimeLineViewModel.class);
//        dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentAddTimeLineBinding.inflate(inflater, container, false);
//        View rootView = binding.getRoot();
//
//        categoryEntities = new ArrayList<>();
//        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), new Observer<List<CategoryEntity>>() {
//            @Override
//            public void onChanged(List<CategoryEntity> categoryEntities) {
//                // 새로운 카테고리들을 추가
//                for (int i = 1; i < categoryEntities.size(); i++) {
//                    Chip chip = new Chip(getContext());
//                    chip.setText(categoryEntities.get(i).getTitle());
//                    chip.setCheckable(true);
//                    binding.cgCategory.addView(chip);
//                }
//            }
//        });
//
//
////        timeLineViewModel.getAllCategories().observe(getViewLifecycleOwner(), new Observer<List<CategoryEntity>>() {
////            @Override
////            public void onChanged(List<CategoryEntity> categoryEntities_value) {
////                if (categoryEntities.size() == 0) {
////                    CategoryEntity add_category = new CategoryEntity("#FFFFFF", "추가하기");
////                    categoryEntities.add(add_category);
////
////                    for (int i = categoryEntities_value.size(); i > 0; i--) {
////                        categoryEntities.add(categoryEntities.size() - 1, categoryEntities_value.get(categoryEntities_value.size() - i));
////                    }
////
////                    categoryAdapter = new CategoryAdapter(categoryEntities, position -> onItemClick(position, categoryEntities.size()));
////                    LinearLayoutManager category_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
////                    binding.rvCategory.setLayoutManager(category_layoutManager);
////                    binding.rvCategory.setAdapter(categoryAdapter);
//////                    Log.d(TAG,"1");
////                }
//////                Log.d(TAG,"2 "+categoryEntities.size()+" "+categoryEntities_value.size());
////                if (categoryEntities_value != null && categoryEntities_value.size() > categoryEntities.size() - 2) {
//////                    Log.d(TAG,"3 "+categoryEntities.size()+" "+categoryEntities_value.size());
////                    categoryEntities.add(categoryEntities.size() - 1, categoryEntities_value.get(categoryEntities_value.size() - 1));
////                    categoryAdapter.notifyItemInserted(categoryEntities.size() - 2);
////                    // 추가된 카테고리로 스크롤
////                    binding.rvCategory.scrollToPosition(categoryEntities.size() - 2);
////                }
////
////            }
////        });
////
////        categoryAdapter = new CategoryAdapter(categoryEntities, position -> onItemClick(position, categoryEntities.size()));
////        LinearLayoutManager category_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
////        binding.rvCategory.setLayoutManager(category_layoutManager);
////        binding.rvCategory.setAdapter(categoryAdapter);
//
//        binding.edtDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatePickerBottomSheetFragment datePickerFragment = new DatePickerBottomSheetFragment();
//                datePickerFragment.setOnDateSelectedListener(AddTimeLineFragment.this);
//                datePickerFragment.show(getParentFragmentManager(), "DatePickerBottomSheetFragment");
//            }
//        });
//
//        binding.cbTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    TimePickerBottomSheetFragment timePickerFragment = new TimePickerBottomSheetFragment();
//                    timePickerFragment.setOnTimeSelectedListener(AddTimeLineFragment.this);
//                    timePickerFragment.show(getParentFragmentManager(), "DatePickerBottomSheetFragment");
//                } else {
//                    binding.cbTime.setText(R.string.not_check_time);
//                }
//            }
//        });
//
//        binding.sliderImportance.addOnChangeListener(new Slider.OnChangeListener() {
//            @Override
//            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
//                binding.edtImportance.setText(String.valueOf((int) value));
//            }
//        });
//
//        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String content = binding.edtContents.getText().toString();
//                String[] str_date = binding.edtDate.getText().toString().split(" ");
//                int year = Integer.valueOf(str_date[0].replace("년", ""));
//                int month = Integer.valueOf(str_date[1].replace("월", ""));
//                int day = Integer.valueOf(str_date[2].replace("일", ""));
//
//                Calendar calendar = Calendar.getInstance();
//                int currentYear = calendar.get(Calendar.YEAR);
//                int currentMonth = calendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 +1 해줍니다.
//                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
//                int currentHour = calendar.get(Calendar.HOUR);
//                int currentMinute = calendar.get(Calendar.MINUTE);
//                int currnetSecond = calendar.get(Calendar.SECOND);
//
//                if (binding.cgCategory.getCheckedChipIds().size() > 0) {
//                    Chip selectedChip = binding.cgCategory.findViewById(binding.cgCategory.getCheckedChipIds().get(0));
//                    int position = binding.cgCategory.indexOfChild(selectedChip);
//                    category_uuid = categoryViewModel.getAllCategories().getValue().get(position + 1).getUuid();
//                    category = categoryViewModel.getAllCategories().getValue().get(position + 1).getTitle();
//                } else {
//                    category_uuid = categoryViewModel.getAllCategories().getValue().get(0).getUuid();
//                    category = categoryViewModel.getAllCategories().getValue().get(0).getTitle();
//                }
//
////                binding.cgCategory.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
////                    @Override
////                    public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
////                        if(checkedIds.size()>0){
////                            Chip selectedChip = group.findViewById(checkedIds.get(0));
////                            Toast.makeText(getContext(), "position: ", Toast.LENGTH_SHORT).show();
////                            if (selectedChip != null) {
////                                int position = group.indexOfChild(selectedChip);
////                                Toast.makeText(getContext(), "position: "+position, Toast.LENGTH_SHORT).show();
////                                category_uuid = categoryViewModel.getAllCategories().getValue().get(position).getUuid();
////                                category = categoryViewModel.getAllCategories().getValue().get(position).getTitle();
////                                Toast.makeText(getContext(), category_uuid, Toast.LENGTH_SHORT).show();
////                                // Now 'position' contains the index of the selected Chip in the ChipGroup
////                                // Use 'position' as needed
////                            }
////                        }
////                    }
////                });
//
//                int importance = (int) binding.sliderImportance.getValue();
//
//                boolean alarm = binding.cbTime.isChecked();
//
//                int alarm_hour = 0;
//                int alarm_minute = 0;
//
//                if (alarm) {
//                    alarm_hour = Integer.parseInt(binding.cbTime.getText().toString().substring(14, 16));
//                    alarm_minute = Integer.parseInt(binding.cbTime.getText().toString().substring(18, 20));
//                }
//
//                TimeLineEntity timeLine = new TimeLineEntity(UUID.randomUUID().toString(), category_uuid, content, year, month, day, currentYear, currentMonth, currentDay, currentHour, currentMinute, currnetSecond, category, importance, alarm, alarm_hour, alarm_minute);
//                timeLineViewModel.insertTimeLine(timeLine);
//
//                Navigation.findNavController(requireView()).navigate(R.id.action_addTimeLineFragment_to_timeLineFragment);
//            }
//        });
//
//
//        return rootView;
//    }
//
//    private void onItemClick(int position, int size) {
//        // 아이템 클릭 이벤트 처리
//        // 다른 화면으로 이동하는 코드 작성 (예: Intent 사용)
////        if (position == size - 1) {
////            Toast.makeText(getContext(), "추가하기", Toast.LENGTH_SHORT).show();
////            CategoryBottomSheetFragment categoryBottomSheetFragment = new CategoryBottomSheetFragment();
////            categoryBottomSheetFragment.setOnCategoryNameSetListener(AddTimeLineFragment.this);
////            categoryBottomSheetFragment.show(getParentFragmentManager(), "CategoryBottomSheetFragment");
////        }
//
////        switch (position) {
////            case 0:
////
////                break;
////            default:
////                Toast.makeText(getContext(), "추가하기", Toast.LENGTH_SHORT).show();
////                CategoryBottomSheetFragment categoryBottomSheetFragment = new CategoryBottomSheetFragment();
////                categoryBottomSheetFragment.setOnCategoryNameSetListener(AddTimeLineFragment.this);
////                categoryBottomSheetFragment.show(getParentFragmentManager(), "CategoryBottomSheetFragment");
////                break;
////        }
//
//
//    }
//
//    @Override
//    public void onDateSelected(String formattedDate) {
//        try {
//            Date selectedDate = dateFormat.parse(formattedDate);
//            Date currentDate = new Date();
//
//            if (selectedDate.before(currentDate)) {
//                binding.cbTime.setChecked(false);
//                binding.cbTime.setEnabled(false);
//            } else {
//                binding.cbTime.setEnabled(true);
//            }
//
//            binding.edtDate.setText(formattedDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void OnTimeSelectedListener(String formattedTime) {
//        binding.cbTime.setText(binding.edtDate.getText() + " " + formattedTime + "에 알림을 보낼게요.");
//    }
//
////    @Override
////    public void onCategoryNameSet(String category_name) {
////        CategoryEntity newCategory = new CategoryEntity(UUID.randomUUID().to"#FFFFFF", category_name);
////
////        timeLineViewModel.insertCategory(newCategory);
////    }
//}
