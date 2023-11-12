package com.xlntsmmr.xlnt_timeline.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xlntsmmr.xlnt_timeline.Adapter.CalendarAdapter;
import com.xlntsmmr.xlnt_timeline.Adapter.ContentAdapter;
import com.xlntsmmr.xlnt_timeline.Adapter.TimeLineAdapter;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.AddBottomSheetFragment;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.AddROFBottomSheetFragment;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.CategoryBottomSheetFragment;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.ShowCategoryBottomFragment;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.ShowContentsBottomFragment;
import com.xlntsmmr.xlnt_timeline.DTO.CalendarDayDTO;
import com.xlntsmmr.xlnt_timeline.DTO.ContentDTO;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.R;
import com.xlntsmmr.xlnt_timeline.DTO.TimeDTO;
import com.xlntsmmr.xlnt_timeline.DTO.TimeLineDTO;
import com.xlntsmmr.xlnt_timeline.Entity.TimeLineEntity;
import com.xlntsmmr.xlnt_timeline.ViewModel.CategoryViewModel;
import com.xlntsmmr.xlnt_timeline.ViewModel.TimeLineViewModel;
import com.xlntsmmr.xlnt_timeline.databinding.FragmentTimeLineBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TimeLineFragment extends Fragment implements AddBottomSheetFragment.OnAddListener, CategoryBottomSheetFragment.OnCategoryNameSetListener, AddROFBottomSheetFragment.OnROFDataListener {

    String TAG = "TimeLineFragment";

    private FragmentTimeLineBinding binding;

    // ViewModel
    private CategoryViewModel categoryViewModel;
    private TimeLineViewModel timeLineViewModel;


    //
    TimeLineAdapter timeLineAdapter;
    ArrayList<CategoryEntity> arr_category;
    ArrayList<TimeLineDTO> arr_content;
    ArrayList<TimeLineEntity> arr_timelineEntity;

    HashMap<CategoryEntity, ArrayList<TimeLineDTO>> arr_ROF;

    // Calendar
    private CalendarAdapter calendarAdapter;
    private ArrayList<CalendarDayDTO> calendarDayDTOS;
    private Calendar calendar;
    private Date now;

    int today_year;
    int today_month;
    int today_day;
    int old_position = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        timeLineViewModel = new ViewModelProvider(this).get(TimeLineViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTimeLineBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();



        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arr_category = new ArrayList<>();
        arr_content = new ArrayList<>();
        arr_ROF = new HashMap<>();

        now = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(now);
        today_year = calendar.get(Calendar.YEAR);
        today_month = calendar.get(Calendar.MONTH);
        today_day = calendar.get(Calendar.DAY_OF_MONTH);

        calendarDayDTOS = generateCalendarDays();
        // 오늘 날짜 선택(1회성)
        for(int i = 0; i< calendarDayDTOS.size(); i++){
            if(calendarDayDTOS.get(i)!=null){
                if (calendarDayDTOS.get(i).getDayOfMonth() == today_day && today_month + 1 == calendarDayDTOS.get(i).getMonth() && today_year == calendarDayDTOS.get(i).getYear()) {
                    calendarDayDTOS.get(i).setSelected(true);
                    old_position = i;
                }
            }
        }

        category_init(today_year, today_month+1, today_day);

        timeLineViewModel.getAllTimelines().observe(getViewLifecycleOwner(), new Observer<List<TimeLineEntity>>() {
            @Override
            public void onChanged(List<TimeLineEntity> timeLineEntities) {
                arr_timelineEntity = new ArrayList<>();
                arr_timelineEntity.addAll(timeLineEntities);

                calendarAdapter = new CalendarAdapter(calendarDayDTOS, arr_timelineEntity);

                binding.rvCalendar.setAdapter(calendarAdapter);
                binding.rvCalendar.setLayoutManager(new GridLayoutManager(getContext(), 7));

                calendarAdapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if (old_position == -1) {
                            calendarDayDTOS.get(position).setSelected(true);
                            old_position = position;
                            calendarAdapter.notifyDataSetChanged();
                        } else {
                            calendarDayDTOS.get(old_position).setSelected(false);
                            calendarDayDTOS.get(position).setSelected(true);
                            old_position = position;
                            calendarAdapter.notifyDataSetChanged();
                        }
                        category_init(calendarDayDTOS.get(position).getYear(), calendarDayDTOS.get(position).getMonth(), calendarDayDTOS.get(position).getDayOfMonth());
//                        Log.d(TAG, "날짜: " + calendarDayDTOS.get(position).getDayOfMonth());
                    }
                });
            }
        });

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView()).navigate(R.id.action_timeLineFragment_to_homeFragment);
            }
        });

        binding.btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                calendarDayDTOS = generateCalendarDays();
                calendarDayDTOS.get(findNotNullDayPosition(calendarDayDTOS)).setSelected(true);
                old_position = findNotNullDayPosition(calendarDayDTOS);
                calendarAdapter = new CalendarAdapter(calendarDayDTOS, arr_timelineEntity);
                binding.rvCalendar.setAdapter(calendarAdapter);
                category_init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, 1);
                calendarAdapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if(old_position == -1){
                            calendarDayDTOS.get(position).setSelected(true);
                            old_position = position;
                            calendarAdapter.notifyDataSetChanged();
                        }else{
                            calendarDayDTOS.get(old_position).setSelected(false);
//                            Log.d(TAG, "old_position" + old_position);
                            calendarDayDTOS.get(position).setSelected(true);
                            old_position = position;
                            calendarAdapter.notifyDataSetChanged();
                        }


                        category_init(calendarDayDTOS.get(position).getYear(), calendarDayDTOS.get(position).getMonth(), calendarDayDTOS.get(position).getDayOfMonth());
//                        Log.d(TAG, "날짜: " + calendarDayDTOS.get(position).getDayOfMonth());
//                        Log.d(TAG, "position" + position);
                    }
                });
            }
        });


        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1); // 다음 달로 이동
                calendarDayDTOS = generateCalendarDays();
                calendarDayDTOS.get(findNotNullDayPosition(calendarDayDTOS)).setSelected(true);
                old_position = findNotNullDayPosition(calendarDayDTOS);
                calendarAdapter = new CalendarAdapter(calendarDayDTOS, arr_timelineEntity);
                binding.rvCalendar.setAdapter(calendarAdapter);
                category_init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, 1);
                calendarAdapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if(old_position == -1){
                            calendarDayDTOS.get(position).setSelected(true);
                            old_position = position;
                            calendarAdapter.notifyDataSetChanged();
                        }else{
                            calendarDayDTOS.get(old_position).setSelected(false);
//                            Log.d(TAG, "old_position" + old_position);
                            calendarDayDTOS.get(position).setSelected(true);
                            old_position = position;
                            calendarAdapter.notifyDataSetChanged();
                        }
                        category_init(calendarDayDTOS.get(position).getYear(), calendarDayDTOS.get(position).getMonth(), calendarDayDTOS.get(position).getDayOfMonth());
//                        Log.d(TAG, "날짜: " + calendarDayDTOS.get(position).getDayOfMonth());
//                        Log.d(TAG, "position" + position);
                    }
                });
            }
        });



        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBottomSheetFragment addBottomSheetFragment = new AddBottomSheetFragment();
                addBottomSheetFragment.setOnAddListener(TimeLineFragment.this);
                addBottomSheetFragment.show(getParentFragmentManager(), "AddBottomSheetFragment");
            }
        });

        binding.btnListMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("category_list", arr_category);

                // Navigation을 통해 ListMoveFragment로 이동합니다.
                Navigation.findNavController(requireView()).navigate(R.id.action_timeLineFragment_to_listMoveDialogFragment, bundle);
            }
        });
    }

    private int findNotNullDayPosition(ArrayList<CalendarDayDTO> calendarDayDTOS){
        for(int i = 0; i< calendarDayDTOS.size(); i++){
            if(calendarDayDTOS.get(i)!=null){
                return i;
            }
        }
        return 0;
    }

    private ArrayList<CalendarDayDTO> generateCalendarDays() {
        ArrayList<CalendarDayDTO> days = new ArrayList<>();

        // 현재 날짜 설정
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = 1; // 첫 번째 날

        binding.tvCurrentYear.setText(String.valueOf(year));
        binding.tvCurrentMonth.setText(String.valueOf(month + 1));

        // 시작일의 요일과 주차 계산
        calendar.set(year, month, dayOfMonth);
        int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 시작일의 요일 (1=일요일, 2=월요일, ...)
        int numberOfWeeksInMonth = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH); // 해당 월의 주차 수
        int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 빈 공간 채우기 (시작일 이전)
        for (int i = 1; i < startDayOfWeek; i++) {
            days.add(null);
        }

        // 날짜 채우기
        for (int i = 1; i <= lastDayOfMonth; i++) {
            days.add(new CalendarDayDTO(year, month + 1, i));
        }

        return days;
    }

    private void category_init(int year, int month, int day) {
//        Log.d(TAG, year+"년"+month+"월"+day+"일");
        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), new Observer<List<CategoryEntity>>() {
            @Override
            public void onChanged(List<CategoryEntity> categoryEntities) {
                arr_category.clear();
                arr_category.addAll(categoryEntities);

                timeLineViewModel.getAllTimelines().observe(getViewLifecycleOwner(), new Observer<List<TimeLineEntity>>() {
                    @Override
                    public void onChanged(List<TimeLineEntity> timeLineEntities) {
//                        Log.d(TAG, "timeLineEntities size: "+timeLineEntities.size());
                        arr_ROF.clear();

                        for (CategoryEntity categoryEntity : categoryEntities) {
                            ArrayList<TimeLineDTO> timelinesForCategory = new ArrayList<>();

                            for (TimeLineEntity timeLineEntity : timeLineEntities) {
                                if (timeLineEntity.getCategory_uuid().equals(categoryEntity.getUuid())) {
                                    TimeDTO timeDTO = new TimeDTO();
                                    timeDTO.setYear(timeLineEntity.getYear());
                                    timeDTO.setMonth(timeLineEntity.getMonth());
                                    timeDTO.setDay(timeLineEntity.getDay());

                                    ContentDTO contentDTO = new ContentDTO();
                                    contentDTO.setStatus(timeLineEntity.getStatus());
                                    contentDTO.setContent(timeLineEntity.getContents());
                                    contentDTO.setCategory_uuid(timeLineEntity.getCategory_uuid());
                                    contentDTO.setRof_uuid(timeLineEntity.getUuid());
                                    contentDTO.setAlarm(timeLineEntity.isAlarm());

                                    TimeLineDTO timeLineDTO = new TimeLineDTO();
                                    timeLineDTO.setUUID(timeLineEntity.getUuid());
                                    timeLineDTO.setTimeDTO(timeDTO);
                                    timeLineDTO.setContentDTO(contentDTO);

                                    if(timeDTO.getYear()==year&&timeDTO.getMonth()==month&&timeDTO.getDay()==day){
                                        timelinesForCategory.add(timeLineDTO);
                                    }
                                }
                            }
                            if(timelinesForCategory.size() > 0){
                                arr_ROF.put(categoryEntity, timelinesForCategory);
                            }
                        }

                        setAdapter(arr_ROF);
                    }
                });
            }
        });
    }


    private void setAdapter(HashMap<CategoryEntity, ArrayList<TimeLineDTO>> arr_ROF) {
        if (arr_category.size() > 0) {
            // Convert the HashMap to a List for sorting
            List<Map.Entry<CategoryEntity, ArrayList<TimeLineDTO>>> list = new ArrayList<>(arr_ROF.entrySet());

            // Sort the list based on CategoryEntity's position
            Collections.sort(list, new Comparator<Map.Entry<CategoryEntity, ArrayList<TimeLineDTO>>>() {
                @Override
                public int compare(Map.Entry<CategoryEntity, ArrayList<TimeLineDTO>> o1, Map.Entry<CategoryEntity, ArrayList<TimeLineDTO>> o2) {
                    return Integer.compare(o1.getKey().getPosition(), o2.getKey().getPosition());
                }
            });

            // Create a new sorted HashMap
            LinkedHashMap<CategoryEntity, ArrayList<TimeLineDTO>> sortedMap = new LinkedHashMap<>();
            for (Map.Entry<CategoryEntity, ArrayList<TimeLineDTO>> entry : list) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }

            timeLineAdapter = new TimeLineAdapter(sortedMap);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            binding.rvTodo.setLayoutManager(layoutManager);
//            timeLineAdapter.contentAdapter = new ContentAdapter(new ArrayList<>());
            binding.rvTodo.setAdapter(timeLineAdapter);
            timeLineAdapter.setOnStatusButtonClickListener(new TimeLineAdapter.OnStatusButtonClickListener() {
                @Override
                public void onStatusButtonClick(String uuid, int status) {
//                    Log.d(TAG, "uuid: " + uuid);
//                    Log.d(TAG, "status: " + status);
                    modifyStatus(uuid, status);
                }
            });

            timeLineAdapter.setOnEdtAndMoreButtonClickListener(new ContentAdapter.OnEdtAndMoreButtonClickListener() {
                @Override
                public void onEdtAndMoreButtonClickListener(String UUID) {
                    ShowContentsBottomFragment showContentsBottomFragment = new ShowContentsBottomFragment(UUID);
//                showContentsBottomFragment.setOnCategoryNameSetListener(HomeFragment.this);
                    showContentsBottomFragment.show(getParentFragmentManager(), "DatePickerBottomSheetFragment");
                }
            });

            timeLineAdapter.setOnCategoryChipLongClickListener(new TimeLineAdapter.OnCategoryChipLongClickListener() {
                @Override
                public void onCategoryChipLongClickListener(String category_UUID, String title) {
                    ShowCategoryBottomFragment showCategoryBottomFragment = new ShowCategoryBottomFragment(category_UUID, title);
                    showCategoryBottomFragment.show(getParentFragmentManager(), "ShowCategoryBottomFragment");
                }
            });
        }
    }

    private void modifyStatus(String uuid, int status){
        int newStatus;

        switch(status) {
            case 0:
                newStatus = 1;
                break;
            case 1:
                newStatus = 2;
                break;
            case 2:
                newStatus = 0;
                break;
            default:
                newStatus = status; // 예외 상황에 대한 처리
                break;
        }

        // 여기서 newStatus를 사용하여 DB 업데이트 작업을 수행합니다.
        // TimeLineRepository 클래스의 메소드를 활용하여 DB 업데이트를 수행합니다.
        timeLineViewModel.updateStatusByUUID(uuid, newStatus);
    }



    @Override
    public void onROFDataListener(TimeLineEntity timeLineEntity) {
        addROF(timeLineEntity);
    }

    @Override
    public void onCategoryNameSet(CategoryEntity categoryEntity) {
        addCategory(categoryEntity);
    }

    private void addROF(TimeLineEntity timeLineEntity) {
        timeLineViewModel.insertTimeLine(timeLineEntity);
    }

    private void addCategory(CategoryEntity categoryEntity) {
        categoryViewModel.insertCategory(categoryEntity);
    }

    @Override
    public void onAddListener(int which) {
        switch (which) {
            case 1:
                CategoryBottomSheetFragment categoryBottomSheetFragment = new CategoryBottomSheetFragment();
                categoryBottomSheetFragment.setOnCategoryNameSetListener(TimeLineFragment.this);
                categoryBottomSheetFragment.show(getParentFragmentManager(), "DatePickerBottomSheetFragment");
                break;
            case 2:
                CalendarDayDTO calendarDayDTO = calendarDayDTOS.get(old_position);
                AddROFBottomSheetFragment addROFBottomSheetFragment = new AddROFBottomSheetFragment(calendarDayDTO.getYear(), calendarDayDTO.getMonth(), calendarDayDTO.getDayOfMonth());
                addROFBottomSheetFragment.setOnROFListener(TimeLineFragment.this);
                addROFBottomSheetFragment.show(getParentFragmentManager(), "DatePickerBottomSheetFragment");
                break;
        }
    }
}