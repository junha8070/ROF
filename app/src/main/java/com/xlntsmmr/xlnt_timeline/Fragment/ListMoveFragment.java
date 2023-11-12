package com.xlntsmmr.xlnt_timeline.Fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xlntsmmr.xlnt_timeline.Adapter.CategoryListMoveAdapter;
import com.xlntsmmr.xlnt_timeline.DialogFragmet.LoadingDialogFragment;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.ItemMoveCallback;
import com.xlntsmmr.xlnt_timeline.R;
import com.xlntsmmr.xlnt_timeline.ViewModel.CategoryViewModel;
import com.xlntsmmr.xlnt_timeline.databinding.FragmentListMoveDialogBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ListMoveFragment extends Fragment {

    private String TAG = "ListMoveFragment";

    private FragmentListMoveDialogBinding binding;
    private CategoryListMoveAdapter adapter;

    private CategoryViewModel categoryViewModel;
    private ArrayList<CategoryEntity> arr_category;
    private LoadingDialogFragment loadingDialog;
    ItemTouchHelper.Callback callback;
    ItemTouchHelper touchHelper;
    ItemMoveCallback itemMoveCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }

//    public ListMoveFragment(ArrayList<CategoryEntity> arr_category) {
//        this.arr_category = arr_category;
//    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListMoveDialogBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Bundle에서 데이터를 받아옵니다.
        arr_category = getArguments().getParcelableArrayList("category_list");

        if (arr_category != null) {

            Collections.sort(arr_category, new Comparator<CategoryEntity>() {
                @Override
                public int compare(CategoryEntity category1, CategoryEntity category2) {
                    return Integer.compare(category1.getPosition(), category2.getPosition());
                }
            });
            adapter = new CategoryListMoveAdapter(arr_category);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            binding.rvCategory.setLayoutManager(layoutManager);
            binding.rvCategory.setAdapter(adapter);
            dismissLoadingDialog();

            callback = new ItemMoveCallback(adapter);
            touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(binding.rvCategory);

            adapter.setOnItemChangeListener(new CategoryListMoveAdapter.OnItemChangeListener() {
                @Override
                public void setPosition(ArrayList<CategoryEntity> change_position_arr_category) {
                    for (int i = 0; i < change_position_arr_category.size(); i++) {
                        Log.d(TAG, "setPosition: " + change_position_arr_category.get(i).getTitle());
                    }
                    requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                        @Override
                        public void handleOnBackPressed() {
                            // Handle the back button press here
                            updateListPosition(change_position_arr_category);
                        }
                    });

                    binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateListPosition(change_position_arr_category);
                        }
                    });
                }
            });
        } else {
            showLoadingDialog();
        }
        return rootView;
    }


    private void updateListPosition(ArrayList<CategoryEntity> change_position_arr_category) {
        for (int i = 0; i < change_position_arr_category.size(); i++) {
            change_position_arr_category.get(i).setPosition(i);
        }

        // 변경된 위치 정보를 Repository에 전달하여 업데이트 수행
        categoryViewModel.updateCategoryPositions(change_position_arr_category);

        Navigation.findNavController(requireView()).navigate(R.id.action_listMoveDialogFragment_to_timeLineFragment);
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