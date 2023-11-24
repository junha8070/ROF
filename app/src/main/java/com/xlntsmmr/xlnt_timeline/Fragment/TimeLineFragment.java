package com.xlntsmmr.xlnt_timeline.Fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xlntsmmr.xlnt_timeline.Adapter.CalendarAdapter;
import com.xlntsmmr.xlnt_timeline.Adapter.ShimmerCalendarAdapter;
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

    // 클래스 멤버 변수 및 상수 정의
    private String TAG = "TimeLineFragment";
    private static final int STATUS_NEW = 0;
    private static final int STATUS_IN_PROGRESS = 1;
    private static final int STATUS_COMPLETED = 2;
    private FragmentTimeLineBinding binding;
    private CategoryViewModel categoryViewModel;
    private TimeLineViewModel timeLineViewModel;
    private TimeLineAdapter timeLineAdapter;
    private ArrayList<CategoryEntity> arr_category;
    private ArrayList<TimeLineDTO> arr_content;
    private ArrayList<TimeLineEntity> arr_timelineEntity;
    private HashMap<CategoryEntity, ArrayList<TimeLineDTO>> arr_ROF;
    private CalendarAdapter calendarAdapter;
    private ShimmerCalendarAdapter shimmerCalendarAdapter;
    private ArrayList<CalendarDayDTO> calendarDayDTOS;
    private Calendar calendar;
    private Date now;
    private int today_year;
    private int today_month;
    private int today_day;
    private int old_position = -1;

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
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Observer 해제
        timeLineViewModel.getAllTimelines().removeObservers(getViewLifecycleOwner());
        Log.d(TAG, "TimelineFragment timeLineViewModel Observer is remove.");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeData();
        setListeners();
    }

    private void initializeData() {
        arr_category = new ArrayList<>();
        arr_content = new ArrayList<>();
        arr_ROF = new HashMap<>();
        initializeDate();
        calendarDayDTOS = generateCalendarDays();
        selectTodayDate();
        category_init(today_year, today_month + 1, today_day);
        observeTimelines();
    }

    private void initializeDate() {
        now = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(now);
        today_year = calendar.get(Calendar.YEAR);
        today_month = calendar.get(Calendar.MONTH);
        today_day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void handlePrevButtonClick() {
        calendar.add(Calendar.MONTH, -1);
        handleDateChange();
    }

    private void handleNextButtonClick() {
        calendar.add(Calendar.MONTH, 1);
        handleDateChange();
    }

    private void handleDateChange() {
        calendarDayDTOS = generateCalendarDays();
        int position = findNotNullDayPosition(calendarDayDTOS);
        calendarDayDTOS.get(position).setSelected(true);
        old_position = position;
        calendarAdapter = new CalendarAdapter(calendarDayDTOS, arr_timelineEntity);
        binding.rvCalendar.setAdapter(calendarAdapter);
        category_init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
        setCalendarAdapterClickListener();
    }

    private void setCalendarAdapter() {
        calendarAdapter = new CalendarAdapter(calendarDayDTOS, arr_timelineEntity);
        binding.rvCalendar.setAdapter(calendarAdapter);
        binding.rvCalendar.setLayoutManager(new GridLayoutManager(getContext(), 7));
    }

    private void observeTimelines() {
        timeLineViewModel.getAllTimelines().observe(getViewLifecycleOwner(), timeLineEntities -> {
            arr_timelineEntity = new ArrayList<>();
            arr_timelineEntity.addAll(timeLineEntities);
            calendarAdapter = new CalendarAdapter(calendarDayDTOS, arr_timelineEntity);
            setCalendarAdapter();
            setCalendarAdapterClickListener();
        });
    }

    private void selectDate(int position) {
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
    }

    private void setOnStatusButtonClickListener() {
        timeLineAdapter.setOnStatusButtonClickListener((uuid, status) -> modifyStatus(uuid, status));
    }

    private void setOnEdtAndMoreButtonClickListener() {
        timeLineAdapter.setOnEdtAndMoreButtonClickListener(UUID -> showContentsBottomSheet(UUID));
    }

    private void setOnCategoryChipLongClickListener() {
        timeLineAdapter.setOnCategoryChipLongClickListener((category_UUID, title) -> showCategoryBottomSheet(category_UUID, title));
    }

    private void showAddBottomSheet() {
        AddBottomSheetFragment addBottomSheetFragment = new AddBottomSheetFragment();
        addBottomSheetFragment.setOnAddListener(this);
        addBottomSheetFragment.show(getParentFragmentManager(), "AddBottomSheetFragment");
    }

    private void navigateToListMoveFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("category_list", arr_category);
        bundle.putString("navigate", "timeline");
        Navigation.findNavController(requireView()).navigate(R.id.action_timeLine_to_listMoveDialog, bundle);
    }

    private void setAdapter(HashMap<CategoryEntity, ArrayList<TimeLineDTO>> arr_ROF) {
        if (arr_category.size() > 0) {
            List<Map.Entry<CategoryEntity, ArrayList<TimeLineDTO>>> list = new ArrayList<>(arr_ROF.entrySet());

            Collections.sort(list, Comparator.comparingInt(o -> o.getKey().getPosition()));

            LinkedHashMap<CategoryEntity, ArrayList<TimeLineDTO>> sortedMap = new LinkedHashMap<>();
            for (Map.Entry<CategoryEntity, ArrayList<TimeLineDTO>> entry : list) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }

            timeLineAdapter = new TimeLineAdapter(sortedMap);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            binding.rvTodo.setLayoutManager(layoutManager);
            binding.rvTodo.setAdapter(timeLineAdapter);
            setOnStatusButtonClickListener();
            setOnEdtAndMoreButtonClickListener();
            setOnCategoryChipLongClickListener();
        }
    }

    private void showCategoryBottomSheet(String category_UUID, String title) {
        ShowCategoryBottomFragment showCategoryBottomFragment = new ShowCategoryBottomFragment(category_UUID, title);
        showCategoryBottomFragment.show(getParentFragmentManager(), "ShowCategoryBottomFragment");
    }

    private void showContentsBottomSheet(String UUID) {
        ShowContentsBottomFragment showContentsBottomFragment = new ShowContentsBottomFragment(UUID);
        showContentsBottomFragment.show(getParentFragmentManager(), "DatePickerBottomSheetFragment");
    }

    private void showAddROFBottomSheet() {
        CalendarDayDTO calendarDayDTO = calendarDayDTOS.get(old_position);
        AddROFBottomSheetFragment addROFBottomSheetFragment = new AddROFBottomSheetFragment(calendarDayDTO.getYear(), calendarDayDTO.getMonth(), calendarDayDTO.getDayOfMonth());
        addROFBottomSheetFragment.setOnROFListener(this);
        addROFBottomSheetFragment.show(getParentFragmentManager(), "DatePickerBottomSheetFragment");
    }

    private void showAddCategoryBottomSheet() {
        CategoryBottomSheetFragment categoryBottomSheetFragment = new CategoryBottomSheetFragment();
        categoryBottomSheetFragment.show(getParentFragmentManager(), "CategoryBottomSheetFragment");
    }

    private void addROF(TimeLineEntity timeLineEntity) {
        timeLineViewModel.insertTimeLine(timeLineEntity);
    }

    private void addCategory(CategoryEntity categoryEntity) {
        categoryViewModel.insertCategory(categoryEntity);
    }


    private void selectTodayDate() {
        for (int i = 0; i < calendarDayDTOS.size(); i++) {
            if (calendarDayDTOS.get(i) != null && calendarDayDTOS.get(i).getDayOfMonth() == today_day
                    && today_month + 1 == calendarDayDTOS.get(i).getMonth() && today_year == calendarDayDTOS.get(i).getYear()) {
                calendarDayDTOS.get(i).setSelected(true);
                old_position = i;
            }
        }
    }


    private void setCalendarAdapterClickListener() {
        calendarAdapter.setOnItemClickListener(position -> selectDate(position));
    }


    private int findNotNullDayPosition(ArrayList<CalendarDayDTO> calendarDayDTOS) {
        for (int i = 0; i < calendarDayDTOS.size(); i++) {
            if (calendarDayDTOS.get(i) != null) {
                return i;
            }
        }
        return 0;
    }

    private ArrayList<CalendarDayDTO> generateCalendarDays() {
        ArrayList<CalendarDayDTO> days = new ArrayList<>();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = 1;

        binding.tvCurrentYear.setText(String.valueOf(year));
        binding.tvCurrentMonth.setText(String.valueOf(month + 1));

        calendar.set(year, month, dayOfMonth);
        int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int numberOfWeeksInMonth = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
        int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 1; i < startDayOfWeek; i++) {
            days.add(null);
        }

        for (int i = 1; i <= lastDayOfMonth; i++) {
            days.add(new CalendarDayDTO(year, month + 1, i));
        }

        return days;
    }

    private void category_init(int year, int month, int day) {
        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), categoryEntities -> {
            arr_category.clear();
            arr_category.addAll(categoryEntities);
            observeTimelinesForCategories(year, month, day, categoryEntities);
        });
    }

    private void observeTimelinesForCategories(int year, int month, int day, List<CategoryEntity> categoryEntities) {
        timeLineViewModel.getAllTimelines().observe(getViewLifecycleOwner(), timeLineEntities -> {
            arr_ROF.clear();

            for (CategoryEntity categoryEntity : categoryEntities) {
                ArrayList<TimeLineDTO> timelinesForCategory = new ArrayList<>();

                for (TimeLineEntity timeLineEntity : timeLineEntities) {
                    if (timeLineEntity.getCategory_uuid().equals(categoryEntity.getUuid())) {
                        TimeDTO timeDTO = createTimeDTO(timeLineEntity);
                        ContentDTO contentDTO = createContentDTO(timeLineEntity);

                        TimeLineDTO timeLineDTO = new TimeLineDTO();
                        timeLineDTO.setUUID(timeLineEntity.getUuid());
                        timeLineDTO.setTimeDTO(timeDTO);
                        timeLineDTO.setContentDTO(contentDTO);

                        if (timeDTO.getYear() == year && timeDTO.getMonth() == month && timeDTO.getDay() == day) {
                            timelinesForCategory.add(timeLineDTO);
                        }
                    }
                }
                if (timelinesForCategory.size() > 0) {
                    arr_ROF.put(categoryEntity, timelinesForCategory);
                }
            }
            setAdapter(arr_ROF);
        });
    }

    private TimeDTO createTimeDTO(TimeLineEntity timeLineEntity) {
        TimeDTO timeDTO = new TimeDTO();
        timeDTO.setYear(timeLineEntity.getYear());
        timeDTO.setMonth(timeLineEntity.getMonth());
        timeDTO.setDay(timeLineEntity.getDay());
        return timeDTO;
    }

    private ContentDTO createContentDTO(TimeLineEntity timeLineEntity) {
        ContentDTO contentDTO = new ContentDTO();
        contentDTO.setStatus(timeLineEntity.getStatus());
        contentDTO.setContent(timeLineEntity.getContents());
        contentDTO.setCategory_uuid(timeLineEntity.getCategory_uuid());
        contentDTO.setRof_uuid(timeLineEntity.getUuid());
        contentDTO.setAlarm(timeLineEntity.isAlarm());
        return contentDTO;
    }

    private void setListeners() {
        setupBackPressedCallback();
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(requireView()).navigate(R.id.action_timeLine_to_home));

        binding.btnPrev.setOnClickListener(v -> handlePrevButtonClick());

        binding.btnNext.setOnClickListener(v -> handleNextButtonClick());

        binding.btnAdd.setOnClickListener(v -> showAddBottomSheet());

        binding.btnListMove.setOnClickListener(v -> navigateToListMoveFragment());
    }

    private void setupBackPressedCallback() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(requireView()).navigate(R.id.action_timeLine_to_home);
            }
        });
    }

    private void modifyStatus(String uuid, int status) {
        int newStatus;
        switch (status) {
            case STATUS_NEW:
                newStatus = STATUS_IN_PROGRESS;
                break;
            case STATUS_IN_PROGRESS:
                newStatus = STATUS_COMPLETED;
                break;
            case STATUS_COMPLETED:
                newStatus = STATUS_NEW;
                break;
            default:
                newStatus = status;
                break;
        }
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


    @Override
    public void onAddListener(int which) {
        switch (which) {
            case 1:
                showAddCategoryBottomSheet();
                break;
            case 2:
                showAddROFBottomSheet();
                break;
        }
    }

//    private void setShimmer() {
//        shimmerCalendarAdapter = new ShimmerCalendarAdapter();
//        binding.rvShimmer.setAdapter(shimmerCalendarAdapter);
//        binding.rvShimmer.setLayoutManager(new GridLayoutManager(getContext(), 7));
//    }
//
//    private void goneShimmer() {
//        binding.shimmerLoading.setVisibility(View.GONE);
//    }
//
//    private void visibleShimmer() {
//        binding.shimmerLoading.setVisibility(View.VISIBLE);
//    }
}