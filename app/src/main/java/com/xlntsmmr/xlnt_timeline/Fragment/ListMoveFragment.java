package com.xlntsmmr.xlnt_timeline.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.xlntsmmr.xlnt_timeline.Adapter.CategoryListMoveAdapter;
import com.xlntsmmr.xlnt_timeline.BottomSheetFragment.ShowCategoryBottomFragment;
import com.xlntsmmr.xlnt_timeline.DialogFragmet.LoadingDialogFragment;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.ItemMoveCallback;
import com.xlntsmmr.xlnt_timeline.R;
import com.xlntsmmr.xlnt_timeline.ViewModel.CategoryViewModel;
import com.xlntsmmr.xlnt_timeline.databinding.FragmentListMoveDialogBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListMoveFragment extends Fragment {

    private String TAG = "ListMoveFragment";
    private FragmentListMoveDialogBinding binding;
    private CategoryListMoveAdapter adapter;
    private CategoryViewModel categoryViewModel;
    private ArrayList<CategoryEntity> arr_category;
    private ArrayList<CategoryEntity> arr_change_category;
    private LoadingDialogFragment loadingDialog;
    private ItemTouchHelper.Callback callback;
    private ItemTouchHelper touchHelper;

    String navigate = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListMoveDialogBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        arr_category = new ArrayList<>();
        arr_change_category = new ArrayList<>();
        navigate = getArguments().getString("navigate");
        navigate();

        binding.btnListMove.setChecked(true); // btnListMove를 선택 상태로 초기화

        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), new Observer<List<CategoryEntity>>() {
            @Override
            public void onChanged(List<CategoryEntity> categoryEntities) {
                if (categoryEntities != null) {
                    arr_category.clear();
                    arr_category.addAll(categoryEntities);
                    arr_category.sort(Comparator.comparingInt(CategoryEntity::getPosition));
                    adapter = new CategoryListMoveAdapter(arr_category);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvCategory.setLayoutManager(layoutManager);
                    binding.rvCategory.setAdapter(adapter);
                    initializeUI(); // 이 부분을 호출하여 초기 UI 설정
                }
            }
        });
        return rootView;
    }

    private void initializeUI() {
        binding.btnListMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchHelper.attachToRecyclerView(null);
                binding.btnListMove.setChecked(true);
                touchHelper = new ItemTouchHelper(new ItemMoveCallback(adapter));
                touchHelper.attachToRecyclerView(binding.rvCategory);
                adapter.setOnItemChangeListener(new CategoryListMoveAdapter.OnItemChangeListener() {
                    @Override
                    public void setPosition(ArrayList<CategoryEntity> change_position_arr_category) {
                        Log.d(TAG, "binding.btnListMove.setOnClickListener Listener");
                        arr_change_category.clear();
                        arr_change_category.addAll(change_position_arr_category);
                    }
                });

                // 리사이클러뷰 아이템 클릭 리스너 제거
                adapter.setOnItemClickListener(null);
            }
        });

        binding.btnListEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnListEdit.setChecked(true);
                if(binding.btnListEdit.isChecked()){
                    updateListPosition(arr_change_category);
                }
                adapter.setOnItemClickListener(new CategoryListMoveAdapter.OnItemClickListener() {
                    @Override
                    public void clickListener(String CategoryUUID, String title) {
                        Log.d(TAG, "binding.btnListEdit.setOnClickListener Listener");
                        // 아이템 클릭 시 ShowCategoryBottomFragment를 띄워서 수정할 수 있도록 처리
                        ShowCategoryBottomFragment showCategoryBottomFragment = new ShowCategoryBottomFragment("ListMove", CategoryUUID, title);
                        showCategoryBottomFragment.show(getParentFragmentManager(), "ShowCategoryBottomFragment");
                    }
                });

                // ItemTouchHelper 해제
                touchHelper.attachToRecyclerView(null);
            }
        });

        if (binding.btnListMove.isChecked()) {
            if(touchHelper==null){
                touchHelper = new ItemTouchHelper(new ItemMoveCallback(adapter));
                touchHelper.attachToRecyclerView(binding.rvCategory);
            }
            adapter.setOnItemChangeListener(new CategoryListMoveAdapter.OnItemChangeListener() {
                @Override
                public void setPosition(ArrayList<CategoryEntity> change_position_arr_category) {
                    Log.d(TAG, "if (binding.btnListMove.isChecked()) Listener");
                    arr_change_category.clear();
                    arr_change_category.addAll(change_position_arr_category);
                }
            });

            // 리사이클러뷰 아이템 클릭 리스너 제거
            adapter.setOnItemClickListener(null);
        }

        if(binding.btnListEdit.isChecked()){
            adapter.setOnItemClickListener(new CategoryListMoveAdapter.OnItemClickListener() {
                @Override
                public void clickListener(String CategoryUUID, String title) {
                    Log.d(TAG, "if(binding.btnListEdit.isChecked()) Listener");
                    // 아이템 클릭 시 ShowCategoryBottomFragment를 띄워서 수정할 수 있도록 처리
                    ShowCategoryBottomFragment showCategoryBottomFragment = new ShowCategoryBottomFragment("ListMove", CategoryUUID, title);
                    showCategoryBottomFragment.show(getParentFragmentManager(), "ShowCategoryBottomFragment");
                }
            });

            // ItemTouchHelper 해제
            touchHelper.attachToRecyclerView(null);
        }
    }


    private void navigate() {
        setupBackPressedCallback();
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateListPosition(arr_change_category);
                navigateToDestination();
            }
        });
    }

    private void setupBackPressedCallback() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                updateListPosition(arr_change_category);
                navigateToDestination();
            }
        });
    }

    private void updateListPosition(ArrayList<CategoryEntity> change_position_arr_category) {
        for (int i = 0; i < change_position_arr_category.size(); i++) {
            change_position_arr_category.get(i).setPosition(i);
        }

        categoryViewModel.updateCategoryPositions(change_position_arr_category);
    }

    private void navigateToDestination() {
        int destinationId = navigate.equals("timeline") ? R.id.action_listMoveDialog_to_timeLine : R.id.action_listMoveDialog_to_home;
        Navigation.findNavController(requireView()).navigate(destinationId);
    }

    private void showLoadingDialog() {
        loadingDialog = new LoadingDialogFragment();
        loadingDialog.show(getChildFragmentManager(), "loading_dialog");
    }

    private void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
