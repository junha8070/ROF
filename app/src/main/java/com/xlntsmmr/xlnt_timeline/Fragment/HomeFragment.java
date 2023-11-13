package com.xlntsmmr.xlnt_timeline.Fragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.xlntsmmr.xlnt_timeline.Adapter.TodayROFAdapter;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.AddBottomSheetFragment;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.AddROFBottomSheetFragment;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.CategoryBottomSheetFragment;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.InfoBottomSheetFragment;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.Entity.TimeLineEntity;
import com.xlntsmmr.xlnt_timeline.ItemSpacingDecoration;
import com.xlntsmmr.xlnt_timeline.R;
import com.xlntsmmr.xlnt_timeline.Repository.RemoteConfigRepository;
import com.xlntsmmr.xlnt_timeline.ViewModel.ConfigViewModel;
import com.xlntsmmr.xlnt_timeline.databinding.FragmentHomeBinding;
import com.xlntsmmr.xlnt_timeline.ViewModel.CategoryViewModel;
import com.xlntsmmr.xlnt_timeline.ViewModel.TimeLineViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements AddBottomSheetFragment.OnAddListener, CategoryBottomSheetFragment.OnCategoryNameSetListener, AddROFBottomSheetFragment.OnROFDataListener {

    String TAG = "HomeFragment";

    private FragmentHomeBinding binding;

    private ConfigViewModel configViewModel;

    private CategoryViewModel categoryViewModel;
    private TimeLineViewModel timeLineViewModel;
    private ArrayList<TimeLineEntity> arr_timelines;

    private Calendar calendar;

    TodayROFAdapter todayROFAdapter;
    ArrayList<TimeLineEntity> today_timeline;
    private Date now;

    int ready_count = 0;
    int onGoing_count = 0;
    int finish_count = 0;

    int prev_timeLineEntities_size = 0;

    String currentVersion;

    enum Status {
        READY, ONGOING, FINISH
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        timeLineViewModel = new ViewModelProvider(this).get(TimeLineViewModel.class);
        configViewModel = new ViewModelProvider(this).get(ConfigViewModel.class);

        // Remote Config 값을 가져옵니다.
        configViewModel.fetchRemoteConfig();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        currentVersion = getVersionName(requireActivity());

        configViewModel.getIsRemoteConfigLoadFinish().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isRemoteConfigLoadFinish) {
                if(isRemoteConfigLoadFinish){
                    if(!configViewModel.getLatestVersion().equals(currentVersion)){
                        if(configViewModel.getForceUpdate()){
                            boolean forceUpdate = configViewModel.getForceUpdate();
                            String latestVersion = configViewModel.getLatestVersion();
                            String minVersion = configViewModel.getMinVersion();
                            String updateNewsJson = configViewModel.getUpdateNewsJson();
                            String jsonLatestVersion = configViewModel.getJsonLatestVersion();
                            String jsonUpdateNews = configViewModel.getJsonUpdateNews();
                            String jsonNewFunction = configViewModel.getJsonNewFunction();

                            InfoBottomSheetFragment infoBottomSheetFragment = new InfoBottomSheetFragment(
                                    currentVersion, forceUpdate, latestVersion, minVersion, jsonLatestVersion, jsonUpdateNews, jsonNewFunction);

                            infoBottomSheetFragment.setCancelable(false);

                            Log.d(TAG, "forceUpdate : "+forceUpdate);
                            Log.d(TAG, "latestVersion : "+latestVersion);
                            Log.d(TAG, "minVersion : "+minVersion);
                            Log.d(TAG, "jsonLatestVersion : "+jsonLatestVersion);
                            Log.d(TAG, "jsonUpdateNews : "+jsonUpdateNews);
                            Log.d(TAG, "jsonNewFunction : "+jsonNewFunction);

                            infoBottomSheetFragment.show(getParentFragmentManager(), "InfoBottomSheetFragment");
                        }
                    }
                }
            }
        });

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        now = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(now);
        arr_timelines = new ArrayList<>();
        today_timeline = new ArrayList<>();

        timeLineViewModel.getAllTimelines().observe(getViewLifecycleOwner(), this::onTimelinesChanged);

        binding.rvTodayRof.addItemDecoration(new ItemSpacingDecoration(0, 10, 0, 0));

        binding.btnAddTimeline.setOnClickListener(v -> {
            AddBottomSheetFragment addBottomSheetFragment = new AddBottomSheetFragment();
            addBottomSheetFragment.setOnAddListener(HomeFragment.this);
            addBottomSheetFragment.show(getParentFragmentManager(), "DatePickerBottomSheetFragment");
        });

        binding.btnTimeline.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_home_to_timeLine);
        });

        binding.mcvReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("status", 0);
                Navigation.findNavController(requireView()).navigate(R.id.action_home_to_statusEntireList, bundle);
            }
        });

        binding.mcvOnGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("status", 1);
                Navigation.findNavController(requireView()).navigate(R.id.action_home_to_statusEntireList, bundle);
            }
        });

        binding.btnListMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), new Observer<List<CategoryEntity>>() {
                    @Override
                    public void onChanged(List<CategoryEntity> categoryEntities) {
                        if(categoryEntities!=null){
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("category_list", (ArrayList<? extends Parcelable>) categoryEntities);
                            bundle.putString("navigate", "home");
                            Navigation.findNavController(requireView()).navigate(R.id.action_home_to_listMoveDialog, bundle);
                        }
                    }
                });
            }
        });

        binding.mcvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("status", 2);
                Navigation.findNavController(requireView()).navigate(R.id.action_home_to_statusEntireList, bundle);
            }
        });

        // rvTodayRof를 초기화하고 설정
        if (todayROFAdapter != null) {
            setupTodayROFAdapter();
        }


        binding.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                configViewModel.getIsRemoteConfigLoadFinish().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isRemoteConfigLoadFinish) {
                        if(isRemoteConfigLoadFinish){
                                currentVersion = getVersionName(requireActivity());
                                boolean forceUpdate = configViewModel.getForceUpdate();
                                String latestVersion = configViewModel.getLatestVersion();
                                String minVersion = configViewModel.getMinVersion();
                                String updateNewsJson = configViewModel.getUpdateNewsJson();
                                String jsonLatestVersion = configViewModel.getJsonLatestVersion();
                                String jsonUpdateNews = configViewModel.getJsonUpdateNews();
                                String jsonNewFunction = configViewModel.getJsonNewFunction();

                                InfoBottomSheetFragment infoBottomSheetFragment = new InfoBottomSheetFragment(
                                        currentVersion, forceUpdate, latestVersion, minVersion, jsonLatestVersion, jsonUpdateNews, jsonNewFunction);

                                Log.d(TAG, "forceUpdate : "+forceUpdate);
                                Log.d(TAG, "latestVersion : "+latestVersion);
                                Log.d(TAG, "minVersion : "+minVersion);
                                Log.d(TAG, "jsonLatestVersion : "+jsonLatestVersion);
                                Log.d(TAG, "jsonUpdateNews : "+jsonUpdateNews);
                                Log.d(TAG, "jsonNewFunction : "+jsonNewFunction);

                                infoBottomSheetFragment.show(getParentFragmentManager(), "InfoBottomSheetFragment");
                        }else{
                                currentVersion = getVersionName(requireActivity());
                                boolean forceUpdate = false;
                                String latestVersion = "버전 확인 중";
                                String minVersion = "버전 확인 중";
                                String updateNewsJson = "버전 확인 중";
                                String jsonLatestVersion = "버전 확인 중";
                                String jsonUpdateNews = "버전 확인 중";
                                String jsonNewFunction = "버전 확인 중";

                                InfoBottomSheetFragment infoBottomSheetFragment = new InfoBottomSheetFragment(
                                        currentVersion, forceUpdate, latestVersion, minVersion, jsonLatestVersion, jsonUpdateNews, jsonNewFunction);

                                Log.d(TAG, "forceUpdate : "+forceUpdate);
                                Log.d(TAG, "latestVersion : "+latestVersion);
                                Log.d(TAG, "minVersion : "+minVersion);
                                Log.d(TAG, "jsonLatestVersion : "+jsonLatestVersion);
                                Log.d(TAG, "jsonUpdateNews : "+jsonUpdateNews);
                                Log.d(TAG, "jsonNewFunction : "+jsonNewFunction);

                                infoBottomSheetFragment.show(getParentFragmentManager(), "InfoBottomSheetFragment");
                        }
                    }
                });





//                Bundle bundle = new Bundle();
//                bundle.putBoolean("forceUpdate",forceUpdate);
//                bundle.putString("latestVersion",latestVersion);
//                bundle.putString("minVersion",minVersion);
//                bundle.putString("jsonLatestVersion",jsonLatestVersion);
//                bundle.putString("jsonUpdateNews",jsonUpdateNews);
//                bundle.putString("jsonNewFunction",jsonNewFunction);



//                InfoBottomSheetFragment infoBottomSheetFragment = new InfoBottomSheetFragment();

            }
        });

//        binding.toolbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                throw new RuntimeException("Test Crash"); // Force a crash
//            }
//        });

//        AdBottomSheetFragment adBottomSheetFragment = new AdBottomSheetFragment();
//        adBottomSheetFragment.show(getParentFragmentManager(), "DatePickerBottomSheetFragment");

        return rootView;
    }

    private void onTimelinesChanged(List<TimeLineEntity> timeLineEntities) {
        ready_count = 0;
        onGoing_count = 0;
        finish_count = 0;
        arr_timelines.clear();
        today_timeline.clear();
        if(prev_timeLineEntities_size<timeLineEntities.size()){
            todayROFAdapter = null;
        }
        prev_timeLineEntities_size = timeLineEntities.size();

        Log.d(TAG, "timeLineEntities.size(): "+timeLineEntities.size());
        if(timeLineEntities.size()==0){
//            binding.tvNothing.setVisibility(View.VISIBLE);
        }else if(timeLineEntities.size()>0){
//            binding.tvNothing.setVisibility(View.INVISIBLE);
            for (TimeLineEntity timeline : timeLineEntities) {
                Status status = getStatusEnum(timeline.getStatus());
//            countStatus(status);

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

                if (isToday(timeline)) {
                    today_timeline.add(timeline);
                }
            }


            updateStatusUIWithAnimation(timeLineEntities.size(), ready_count, onGoing_count, finish_count);

            if (todayROFAdapter != null) {
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
            }else{
                setupTodayROFAdapter();
            }
        }
    }

    private Status getStatusEnum(int status) {
        switch (status) {
            case 0:
                return Status.READY;
            case 1:
                return Status.ONGOING;
            case 2:
                return Status.FINISH;
            default:
                throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    private void setupTodayROFAdapter() {
        Log.d(TAG, "setupTodayROFAdapter");
        Log.d(TAG, "setupTodayROFAdapter : "+today_timeline.size());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvTodayRof.setLayoutManager(layoutManager);

        if(today_timeline.size()>0){
            binding.tvNothing.setVisibility(View.INVISIBLE);
        }

        todayROFAdapter = new TodayROFAdapter(today_timeline);
        binding.rvTodayRof.setAdapter(todayROFAdapter);

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
    }

    private int findPositionByUUID(String uuid) {
        for (int i = 0; i < today_timeline.size(); i++) {
            if (today_timeline.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
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

    private void updateStatusUIWithAnimation(int size, int ready_size, int onGoing_size, int finish_size) {
        // 기존의 코드를 이용하여 UI 업데이트
        binding.tvCount.setText(String.valueOf(size));
        binding.tvReady.setText(String.valueOf(ready_count));
        binding.tvOnGing.setText(String.valueOf(onGoing_count));
        binding.tvFinish.setText(String.valueOf(finish_count));

        // 카운팅 애니메이션 적용
        int startValue = 0;

        ValueAnimator tv_count_animator = ValueAnimator.ofInt(startValue, size);
        ValueAnimator ready_animator = ValueAnimator.ofInt(startValue, ready_size);
        ValueAnimator onGoing_animator = ValueAnimator.ofInt(startValue, onGoing_size);
        ValueAnimator finish_animator = ValueAnimator.ofInt(startValue, finish_size);

        int animationDuration = getResources().getInteger(R.integer.animation_duration);
        tv_count_animator.setDuration(animationDuration);
        ready_animator.setDuration(animationDuration);
        onGoing_animator.setDuration(animationDuration);
        finish_animator.setDuration(animationDuration);

        // 값이 변경될 때마다 호출되는 리스너 설정
        tv_count_animator.addUpdateListener(valueAnimator -> {
            int animatedValue = (int) valueAnimator.getAnimatedValue();
            binding.tvCount.setText(String.valueOf(animatedValue));
        });
        ready_animator.addUpdateListener(valueAnimator -> {
            int animatedValue = (int) valueAnimator.getAnimatedValue();
            binding.tvReady.setText(String.valueOf(animatedValue));
        });
        onGoing_animator.addUpdateListener(valueAnimator -> {
            int animatedValue = (int) valueAnimator.getAnimatedValue();
            binding.tvOnGing.setText(String.valueOf(animatedValue));
        });
        finish_animator.addUpdateListener(valueAnimator -> {
            int animatedValue = (int) valueAnimator.getAnimatedValue();
            binding.tvFinish.setText(String.valueOf(animatedValue));
        });

        tv_count_animator.start();
        ready_animator.start();
        onGoing_animator.start();
        finish_animator.start();
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

}