package com.xlntsmmr.xlnt_timeline.Fragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.Printer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.xlntsmmr.xlnt_timeline.Adapter.TodayROFAdapter;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.AddBottomSheetFragment;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.AddROFBottomSheetFragment;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.CategoryBottomSheetFragment;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.InfoBottomSheetFragment;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.ShowContentsBottomFragment;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.Entity.TimeLineEntity;
import com.xlntsmmr.xlnt_timeline.ItemSpacingDecoration;
import com.xlntsmmr.xlnt_timeline.R;
import com.xlntsmmr.xlnt_timeline.ViewModel.CategoryViewModel;
import com.xlntsmmr.xlnt_timeline.ViewModel.ConfigViewModel;
import com.xlntsmmr.xlnt_timeline.ViewModel.TimeLineViewModel;
import com.xlntsmmr.xlnt_timeline.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

public class HomeFragment extends Fragment implements AddBottomSheetFragment.OnAddListener, CategoryBottomSheetFragment.OnCategoryNameSetListener, AddROFBottomSheetFragment.OnROFDataListener {

    private String tag = "HomeFragment";

    private static final int STATUS_READY = 0;
    private static final int STATUS_ONGOING = 1;
    private static final int STATUS_FINISH = 2;

    private FragmentHomeBinding binding;


    private ConfigViewModel configViewModel;
    private CategoryViewModel categoryViewModel;
    private TimeLineViewModel timeLineViewModel;


    private ArrayList<TimeLineEntity> arr_timelines;
    private ArrayList<TimeLineEntity> today_timeline;
    private List<TimeLineEntity> previousTimelines;
    private ArrayList<CategoryEntity> arr_category;


    private Calendar calendar;
    private TodayROFAdapter todayROFAdapter;
    private Date now;


    private int ready_count = 0;
    private int onGoing_count = 0;
    private int finish_count = 0;
    private int prev_timeLineEntities_size = 0;
    private String currentVersion;


    enum Status {
        READY, ONGOING, FINISH
    }

    private Status getStatusEnum(int status) {
        switch (status) {
            case STATUS_READY:
                return Status.READY;
            case STATUS_ONGOING:
                return Status.ONGOING;
            case STATUS_FINISH:
                return Status.FINISH;
            default:
                throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    private void updateStatusCount(Status status) {
        switch (status) {
            case READY:
                ready_count++;
                break;
            case ONGOING:
                onGoing_count++;
                break;
            case FINISH:
                finish_count++;
                break;
        }
    }

    private void updateUIWithCounts(int totalSize) {
        binding.tvCount.setText(String.valueOf(totalSize));
        binding.tvReady.setText(String.valueOf(ready_count));
        binding.tvOnGing.setText(String.valueOf(onGoing_count));
        binding.tvFinish.setText(String.valueOf(finish_count));
    }

    private void initTodayROFAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvTodayRof.setLayoutManager(new GridLayoutManager(getContext(), 1));

        todayROFAdapter = new TodayROFAdapter(today_timeline);
        binding.rvTodayRof.setAdapter(todayROFAdapter);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewModels();
        configViewModel.fetchRemoteConfig();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        currentVersion = getVersionName(requireActivity());
        checkAndUpdateInfoBottomSheet();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        now = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(now);
        arr_timelines = new ArrayList<>();
        today_timeline = new ArrayList<>();
        arr_category = new ArrayList<>();

//        timeLineViewModel.getAllTimelines().observe(getViewLifecycleOwner(), this::onTimelinesChanged);
        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), new Observer<List<CategoryEntity>>() {
            @Override
            public void onChanged(List<CategoryEntity> categoryEntities) {
                if(categoryEntities!=null){
                    arr_category.addAll(categoryEntities);
                    timeLineViewModel.getAllTimelines().observe(getViewLifecycleOwner(), timelines -> {
                        if (timelines != null) {
                            for (int i = 0; i < timelines.size(); i++) {
                                Log.d("HomeFragment ViewModel", timelines.get(i).getContents() + " : " + timelines.get(i).getStatus());
                            }
                            onTimelinesChanged(timelines);
                        }
                    });
                }
            }
        });

        initTodayROFAdapter();

        binding.rvTodayRof.addItemDecoration(new ItemSpacingDecoration(5, 5, 0, 10));

        setClickListeners();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Observer 해제
        timeLineViewModel.getAllTimelines().removeObservers(getViewLifecycleOwner());
        Log.d(tag, "Observer is remove.");
    }


    private void updateCountsAndLists(List<TimeLineEntity> timeLineEntities) {
        ready_count = 0;
        onGoing_count = 0;
        finish_count = 0;
        arr_timelines.clear();
        today_timeline.clear();

        if (prev_timeLineEntities_size < timeLineEntities.size()) {
            todayROFAdapter = null;
        }
        prev_timeLineEntities_size = timeLineEntities.size();

//        Log.d(tag, "timeLineEntities.size(): " + timeLineEntities.size());

        if (!timeLineEntities.isEmpty()) {
            for (TimeLineEntity timeline : timeLineEntities) {
                Status status = getStatusEnum(timeline.getStatus());
                updateStatusCount(status);

                if (isToday(timeline)) {
                    // TODO: 코드 추가

                    today_timeline.add(timeline);
                }
            }


            Collections.sort(today_timeline, (timeline1, timeline2) -> {
                int position1 = getCategoryPositionById(timeline1.getCategory_uuid());
                int position2 = getCategoryPositionById(timeline2.getCategory_uuid());
                return Integer.compare(position1, position2);
            });


            updateUIWithCounts(timeLineEntities.size());
            setupOrUpdateTodayROFAdapter();
        }


//        binding.visibleToggle.check(binding.btnReadyVisible.getId());
//        binding.visibleToggle.check(binding.btnOngoingVisible.getId());
//        binding.visibleToggle.check(binding.btnFinishVisible.getId());
//
//        binding.visibleToggle.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
//            @Override
//            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
//                if (isChecked) {
//                    // 선택된 경우
//                    if (checkedId == binding.btnReadyVisible.getId()) {
//                        binding.btnReadyVisible.setAlpha(1);
//                    } else if (checkedId == binding.btnOngoingVisible.getId()) {
//                        binding.btnOngoingVisible.setAlpha(1);
//                    } else if (checkedId == binding.btnFinishVisible.getId()) {
//                        binding.btnFinishVisible.setAlpha(1);
//                    }
//                } else {
//                    // 선택이 해제된 경우
//                    if (checkedId == binding.btnReadyVisible.getId()) {
//                        binding.btnReadyVisible.setAlpha(0.5f);
//                    } else if (checkedId == binding.btnOngoingVisible.getId()) {
//                        binding.btnOngoingVisible.setAlpha(0.5f);
//                    } else if (checkedId == binding.btnFinishVisible.getId()) {
//                        binding.btnFinishVisible.setAlpha(0.5f);
//                    }
//                }
//            }
//        });

    }

    // getCategoryPositionById 메서드 추가
    private int getCategoryPositionById(String categoryId) {
        for (int i = 0; i < arr_category.size(); i++) {
            if (arr_category.get(i).getUuid().equals(categoryId)) {
                return arr_category.get(i).getPosition();
            }
        }

        return -1; // 해당 categoryId를 가진 카테고리를 찾지 못한 경우
    }


    private void setupOrUpdateTodayROFAdapter() {
        if (todayROFAdapter == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            binding.rvTodayRof.setLayoutManager(new GridLayoutManager(getContext(), 1));

//            Collections.sort(today_timeline, (timeline1, timeline2) -> {
//                int position1 = getCategoryPositionById(timeline1.getCategory_uuid());
//                int position2 = getCategoryPositionById(timeline2.getCategory_uuid());
//                return Integer.compare(position1, position2);
//            });

            todayROFAdapter = new TodayROFAdapter(today_timeline);
            binding.rvTodayRof.setAdapter(todayROFAdapter);
        }

        todayROFAdapter.setListener((uuid, status) -> {
            int newStatus;
            switch (status) {
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
                    newStatus = status; // Handle exception situation
                    break;
            }

            int position = findPositionByUUID(uuid);
            if (position >= 0) {
                todayROFAdapter.updateStatus(position, newStatus);
                timeLineViewModel.updateStatusByUUID(uuid, newStatus);
            }
        });

        todayROFAdapter.setLongClickListener(uuid -> {
            showContentsBottomFragment(uuid);
            todayROFAdapter = null;
        });
    }

    private void onTimelinesChanged(List<TimeLineEntity> timeLineEntities) {
        updateCountsAndLists(timeLineEntities);
        for (int i = 0; i < timeLineEntities.size(); i++) {
            Log.d("HomeFragment", timeLineEntities.get(i).getContents() + " : " + timeLineEntities.get(i).getStatus());
        }

    }


    private int findPositionByUUID(String uuid) {
        return IntStream.range(0, today_timeline.size())
                .filter(i -> today_timeline.get(i).getUuid().equals(uuid))
                .findFirst()
                .orElse(-1);
    }


    private boolean isToday(TimeLineEntity timeline) {
        return calendar.get(Calendar.YEAR) == timeline.getYear() &&
                calendar.get(Calendar.MONTH) + 1 == timeline.getMonth() &&
                calendar.get(Calendar.DAY_OF_MONTH) == timeline.getDay();
    }

    @Override
    public void onAddListener(int which) {
        switch (which) {
            case 1:
                CategoryBottomSheetFragment categoryBottomSheetFragment = new CategoryBottomSheetFragment();
                categoryBottomSheetFragment.setOnCategoryNameSetListener(HomeFragment.this);
                categoryBottomSheetFragment.show(getParentFragmentManager(), "DatePickerBottomSheetFragment");
                break;
            case 2:
                AddROFBottomSheetFragment addROFBottomSheetFragment = new AddROFBottomSheetFragment();
                addROFBottomSheetFragment.setOnROFListener(HomeFragment.this);
                addROFBottomSheetFragment.show(getParentFragmentManager(), "DatePickerBottomSheetFragment");
                break;
        }
    }

    public void showContentsBottomFragment(String uuid) {
        ShowContentsBottomFragment showContentsBottomFragment = new ShowContentsBottomFragment(uuid);
        showContentsBottomFragment.show(getParentFragmentManager(), "ShowContentsBottomFragment");
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

    // 버전 코드를 가져오는 함수 정의
    private String getVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeViewModels() {
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        categoryViewModel = viewModelProvider.get(CategoryViewModel.class);
        timeLineViewModel = viewModelProvider.get(TimeLineViewModel.class);
        configViewModel = viewModelProvider.get(ConfigViewModel.class);
    }


    private void setClickListeners() {
        binding.btnAddTimeline.setOnClickListener(v -> {
            AddBottomSheetFragment addBottomSheetFragment = new AddBottomSheetFragment();
            addBottomSheetFragment.setOnAddListener(HomeFragment.this);
            addBottomSheetFragment.show(getParentFragmentManager(), "DatePickerBottomSheetFragment");
        });

        binding.btnTimeline.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(R.id.action_home_to_timeLine));

        setNavigationClickListener(binding.mcvReady, 0);
        setNavigationClickListener(binding.mcvOnGoing, 1);
        setNavigationClickListener(binding.mcvFinish, 2);

        binding.btnInfo.setOnClickListener(v -> showInfoBottomSheet());

        binding.btnListMove.setOnClickListener(v -> navigateToListMoveDialog());
    }

    private void setNavigationClickListener(View view, int status) {
        view.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("status", status);
            Navigation.findNavController(requireView()).navigate(R.id.action_home_to_statusEntireList, bundle);
        });
    }

    private void navigateToListMoveDialog() {
        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), categoryEntities -> {
            if (categoryEntities != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("category_list", new ArrayList<>(categoryEntities));
                bundle.putString("navigate", "home");
                Navigation.findNavController(requireView()).navigate(R.id.action_home_to_listMoveDialog, bundle);
            }
        });
    }

    // showInfoBottomSheet() 메서드 수정
    private void showInfoBottomSheet() {
        currentVersion = getVersionName(requireActivity());
        boolean isRemoteConfigLoadFinish = Boolean.TRUE.equals(configViewModel.getIsRemoteConfigLoadFinish().getValue());
        boolean forceUpdate = configViewModel.getForceUpdate();

        String latestVersion = isRemoteConfigLoadFinish ? configViewModel.getLatestVersion() : "버전 확인 중";
        String minVersion = isRemoteConfigLoadFinish ? configViewModel.getMinVersion() : "버전 확인 중";
        String updateNewsJson = isRemoteConfigLoadFinish ? configViewModel.getUpdateNewsJson() : "버전 확인 중";
        String jsonLatestVersion = isRemoteConfigLoadFinish ? configViewModel.getJsonLatestVersion() : "버전 확인 중";
        String jsonUpdateNews = isRemoteConfigLoadFinish ? configViewModel.getJsonUpdateNews() : "버전 확인 중";
        String jsonNewFunction = isRemoteConfigLoadFinish ? configViewModel.getJsonNewFunction() : "버전 확인 중";

        InfoBottomSheetFragment infoBottomSheetFragment = new InfoBottomSheetFragment(
                currentVersion, forceUpdate, latestVersion, minVersion, jsonLatestVersion, jsonUpdateNews, jsonNewFunction);

        // forceUpdate가 true일 때 setCancelable(false) 적용
        if (forceUpdate && !latestVersion.equals(currentVersion)) {
            infoBottomSheetFragment.setCancelable(false);
        }

        infoBottomSheetFragment.show(getParentFragmentManager(), "InfoBottomSheetFragment");
    }

    // checkAndUpdateInfoBottomSheet() 메서드 수정
    private void checkAndUpdateInfoBottomSheet() {
        configViewModel.getIsRemoteConfigLoadFinish().observe(getViewLifecycleOwner(), isRemoteConfigLoadFinish -> {
            // Check the condition to show the InfoBottomSheetFragment
            boolean shouldShowInfoBottomSheet = isRemoteConfigLoadFinish &&
                    (configViewModel.getForceUpdate() && !configViewModel.getLatestVersion().equals(currentVersion));

            if (shouldShowInfoBottomSheet) {
                // If the condition is true, show the InfoBottomSheetFragment
                showInfoBottomSheet();
            } else {
                // Condition not met, do not show the InfoBottomSheetFragment
            }
        });
    }


}