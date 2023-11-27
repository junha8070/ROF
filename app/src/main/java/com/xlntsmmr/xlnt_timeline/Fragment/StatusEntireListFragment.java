package com.xlntsmmr.xlnt_timeline.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xlntsmmr.xlnt_timeline.Adapter.TimeLineAdapter;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.ShowCategoryBottomFragment;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.ShowContentsBottomFragment;
import com.xlntsmmr.xlnt_timeline.DTO.ContentDTO;
import com.xlntsmmr.xlnt_timeline.DTO.TimeDTO;
import com.xlntsmmr.xlnt_timeline.DTO.TimeLineDTO;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.Entity.TimeLineEntity;
import com.xlntsmmr.xlnt_timeline.R;
import com.xlntsmmr.xlnt_timeline.ViewModel.CategoryViewModel;
import com.xlntsmmr.xlnt_timeline.ViewModel.TimeLineViewModel;
import com.xlntsmmr.xlnt_timeline.databinding.FragmentStatusEntireListBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StatusEntireListFragment extends Fragment {

    private static final String TAG = "StatusEntireListFragment";

    private FragmentStatusEntireListBinding binding;

    private CategoryViewModel categoryViewModel;
    private TimeLineViewModel timeLineViewModel;

    // 데이터 구조 변경
    private List<TimeLineDTO> timelineList = new ArrayList<>();
    private List<CategoryEntity> categoryList = new ArrayList<>();
    private Map<CategoryEntity, ArrayList<TimeLineDTO>> categoryTimelineMap = new HashMap<>();

    TimeLineAdapter timeLineAdapter;


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
        binding = FragmentStatusEntireListBinding.inflate(inflater, container, false);

        setListeners();
        setupToolbar(getStatusFromArguments());

        return binding.getRoot();
    }

    private int getStatusFromArguments() {
        Bundle bundle = getArguments();
        return bundle != null ? bundle.getInt("status", -1) : -1;
    }

    private void setupToolbar(int status) {
        int colorResId;
        int titleResId;

        switch (status) {
            case 0:
                colorResId = R.color.ready_gray;
                titleResId = R.string.ready;
                getStatusTimeLine(0);
                break;
            case 1:
                colorResId = R.color.onGoing_green;
                titleResId = R.string.onGoing;
                getStatusTimeLine(1);
                break;
            case 2:
                colorResId = R.color.finish_blue;
                titleResId = R.string.finish;
                getStatusTimeLine(2);
                break;
            default:
                return;
        }

        int color = getResources().getColor(colorResId);
        binding.CollapsingToolbarLayout.setContentScrimColor(color);
        binding.mcv.setCardBackgroundColor(color);
        binding.toolbar.setTitle(getString(titleResId));
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        getStatusTimeLine(status);
    }

    private void setListeners() {
        binding.btnClose.setOnClickListener(v ->
                Navigation.findNavController(getView()).navigate(R.id.action_statusEntireList_to_home)
        );
    }

    private void processTimelineEntities(int status, List<TimeLineEntity> timeLineEntities, CategoryEntity categoryEntity) {
        for (TimeLineEntity timeLineEntity : timeLineEntities) {
            if (timeLineEntity.getStatus() == status && categoryEntity.getUuid().equals(timeLineEntity.getCategory_uuid())) {
                createAndAddTimelineDTO(timeLineEntity, categoryEntity);
            }
        }

        if (!timelineList.isEmpty()) {
            categoryTimelineMap.put(categoryEntity, new ArrayList<>(timelineList));
            timelineList.clear();
        }
    }


    private void getStatusTimeLine(int status) {
        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), categoryEntities -> {
            processCategories(status, categoryEntities);
        });
    }


    private void processCategories(int status, List<CategoryEntity> categoryEntities) {
        categoryList = new ArrayList<>(categoryEntities);
        Log.d(TAG, "Category list size: " + categoryList.size());

        timeLineViewModel.getAllTimelines().observe(getViewLifecycleOwner(), timeLineEntities -> {
            categoryTimelineMap = new HashMap<>();
            timelineList = new ArrayList<>();

            for (CategoryEntity categoryEntity : categoryList) {
                processTimelineEntities(status, timeLineEntities, categoryEntity);
            }
            setAdapter();
        });
    }


    private void setAdapter() {
        List<Map.Entry<CategoryEntity, ArrayList<TimeLineDTO>>> list = new ArrayList<>(categoryTimelineMap.entrySet());

        // Sort the list based on CategoryEntity's position
        Collections.sort(list, (o1, o2) -> Integer.compare(o1.getKey().getPosition(), o2.getKey().getPosition()));

        // Create a new sorted HashMap
        LinkedHashMap<CategoryEntity, ArrayList<TimeLineDTO>> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<CategoryEntity, ArrayList<TimeLineDTO>> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        timeLineAdapter = new TimeLineAdapter(sortedMap);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvContents.setLayoutManager(layoutManager);
        binding.rvContents.setAdapter(timeLineAdapter);

        timeLineAdapter.setOnStatusButtonClickListener((uuid, status) -> {
            Log.d(TAG, "uuid: " + uuid);
            Log.d(TAG, "status: " + status);
            modifyStatus(uuid, status);
        });

        timeLineAdapter.setOnEdtAndMoreButtonClickListener(UUID -> {
            ShowContentsBottomFragment showContentsBottomFragment = new ShowContentsBottomFragment(UUID);
            showContentsBottomFragment.show(getParentFragmentManager(), "DatePickerBottomSheetFragment");
        });

        timeLineAdapter.setOnCategoryChipLongClickListener((category_UUID, title) -> {
            ShowCategoryBottomFragment showCategoryBottomFragment = new ShowCategoryBottomFragment(category_UUID, title);
            showCategoryBottomFragment.show(getParentFragmentManager(), "ShowCategoryBottomFragment");
        });
    }


    private void modifyStatus(String uuid, int status) {
        int newStatus = (status + 1) % 3;
        timeLineViewModel.updateStatusByUUID(uuid, newStatus);
    }

    private void createAndAddTimelineDTO(TimeLineEntity timeLineEntity, CategoryEntity categoryEntity) {
        // Use timeLineEntity directly instead of fetching it again from the list
        TimeDTO timeDTO = createTimeDTO(timeLineEntity);
        ContentDTO contentDTO = createContentDTO(timeLineEntity);

        TimeLineDTO timeLineDTO = new TimeLineDTO();
        timeLineDTO.setUUID(timeLineEntity.getUuid());
        timeLineDTO.setTimeDTO(timeDTO);
        timeLineDTO.setContentDTO(contentDTO);
        timelineList.add(timeLineDTO);
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
}