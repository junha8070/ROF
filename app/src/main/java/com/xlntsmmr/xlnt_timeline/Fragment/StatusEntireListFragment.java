package com.xlntsmmr.xlnt_timeline.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xlntsmmr.xlnt_timeline.Adapter.ContentAdapter;
import com.xlntsmmr.xlnt_timeline.Adapter.TimeLineAdapter;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.ShowCategoryBottomFragment;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.ShowContentsBottomFragment;
import com.xlntsmmr.xlnt_timeline.DTO.ContentDTO;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.Entity.TimeLineEntity;
import com.xlntsmmr.xlnt_timeline.R;
import com.xlntsmmr.xlnt_timeline.DTO.TimeDTO;
import com.xlntsmmr.xlnt_timeline.DTO.TimeLineDTO;
import com.xlntsmmr.xlnt_timeline.databinding.FragmentStatusEntireListBinding;
import com.xlntsmmr.xlnt_timeline.ViewModel.CategoryViewModel;
import com.xlntsmmr.xlnt_timeline.ViewModel.TimeLineViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatusEntireListFragment extends Fragment {

    private String TAG = "StatusEntireListFragment";

    private FragmentStatusEntireListBinding binding;

    private CategoryViewModel categoryViewModel;
    private TimeLineViewModel timeLineViewModel;

    ArrayList<TimeLineDTO> arr_timeLineDTOS;
    ArrayList<CategoryEntity> arr_categoryEntities;
    HashMap<CategoryEntity, ArrayList<TimeLineDTO>> arr_ROF;

    TimeLineAdapter timeLineAdapter;
    ContentAdapter contentAdapter;

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
        View rootView = binding.getRoot();

        Bundle bundle = getArguments();
        int status = bundle.getInt("status");

        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_statusEntireListFragment_to_homeFragment);
            }
        });

        switch (status) {
            case 0:
                binding.CollapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.ready_gray));
                binding.mcv.setCardBackgroundColor(getResources().getColor(R.color.ready_gray));
                binding.toolbar.setTitle("Ready");
                binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
                getStatusTimeLine(0);
                break;
            case 1:
                binding.CollapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.onGoing_green));
                binding.mcv.setCardBackgroundColor(getResources().getColor(R.color.onGoing_green));
                binding.toolbar.setTitle("On Going");
                binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
                getStatusTimeLine(1);
                break;
            case 2:
                binding.CollapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.finish_blue));
                binding.mcv.setCardBackgroundColor(getResources().getColor(R.color.finish_blue));
                binding.toolbar.setTitle("Finish");
                binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
                getStatusTimeLine(2);
                break;
            default:
                break;
        }

        return rootView;
    }

    private void getStatusTimeLine(int status) {
        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), new Observer<List<CategoryEntity>>() {
            @Override
            public void onChanged(List<CategoryEntity> categoryEntities) {
                arr_timeLineDTOS = new ArrayList<>();
                arr_ROF = new HashMap<>();
                arr_categoryEntities = new ArrayList<>();
                arr_categoryEntities.addAll(categoryEntities);
                Log.d(TAG, String.valueOf(arr_categoryEntities.size()));
                timeLineViewModel.getAllTimelines().observe(getViewLifecycleOwner(), new Observer<List<TimeLineEntity>>() {
                    @Override
                    public void onChanged(List<TimeLineEntity> timeLineEntities) {
                        for (int r = 0; r < arr_categoryEntities.size(); r++) {
                            for (int i = 0; i < timeLineEntities.size(); i++) {
                                if (timeLineEntities.get(i).getStatus() == status) {
                                    if (arr_categoryEntities.get(r).getUuid().equals(timeLineEntities.get(i).getCategory_uuid())) {

                                        TimeLineEntity timeLine = timeLineEntities.get(i);

                                        TimeDTO timeDTO = new TimeDTO();
                                        timeDTO.setYear(timeLine.getYear());
                                        timeDTO.setMonth(timeLine.getMonth());
                                        timeDTO.setDay(timeLine.getDay());

                                        ContentDTO contentDTO = new ContentDTO();
                                        contentDTO.setStatus(timeLine.getStatus());
                                        contentDTO.setContent(timeLine.getContents());
                                        contentDTO.setCategory_uuid(timeLine.getCategory_uuid());
                                        contentDTO.setRof_uuid(timeLine.getUuid());
                                        contentDTO.setAlarm(timeLine.isAlarm());

                                        TimeLineDTO timeLineDTO = new TimeLineDTO();
                                        timeLineDTO.setUUID(timeLine.getUuid());
                                        timeLineDTO.setTimeDTO(timeDTO);
                                        timeLineDTO.setContentDTO(contentDTO);
                                        arr_timeLineDTOS.add(timeLineDTO);
                                    }
                                }
                            }
                            arr_ROF.put(arr_categoryEntities.get(r), arr_timeLineDTOS);
                            arr_timeLineDTOS = new ArrayList<>();
                        }
                        setAdapter();
                    }
                });
            }
        });


    }

    private void setAdapter() {
        timeLineAdapter = new TimeLineAdapter(arr_ROF);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvContents.setLayoutManager(layoutManager);
        binding.rvContents.setAdapter(timeLineAdapter);

        timeLineAdapter.setOnStatusButtonClickListener(new TimeLineAdapter.OnStatusButtonClickListener() {
            @Override
            public void onStatusButtonClick(String uuid, int status) {
                Log.d(TAG, "uuid: " + uuid);
                Log.d(TAG, "status: " + status);
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
}