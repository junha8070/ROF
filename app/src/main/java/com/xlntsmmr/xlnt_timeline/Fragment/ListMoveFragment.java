package com.xlntsmmr.xlnt_timeline.Fragment;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xlntsmmr.xlnt_timeline.Adapter.CategoryListMoveAdapter;
import com.xlntsmmr.xlnt_timeline.DialogFragmet.LoadingDialogFragment;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.ItemMoveCallback;
import com.xlntsmmr.xlnt_timeline.R;
import com.xlntsmmr.xlnt_timeline.ViewModel.CategoryViewModel;
import com.xlntsmmr.xlnt_timeline.databinding.FragmentListMoveDialogBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

        arr_category = getArguments().getParcelableArrayList("category_list");
        navigate = getArguments().getString("navigate");

        if (arr_category != null) {
            initializeUI();
        } else {
            Toast.makeText(getContext(), R.string.listMoveFragmentError, Toast.LENGTH_SHORT).show();
            navigateToDestination();
        }

        return rootView;
    }

    private void initializeUI() {
        arr_change_category = new ArrayList<>(arr_category);
        arr_category.sort(Comparator.comparingInt(CategoryEntity::getPosition));

        adapter = new CategoryListMoveAdapter(arr_category);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvCategory.setLayoutManager(layoutManager);
        binding.rvCategory.setAdapter(adapter);
        dismissLoadingDialog();

        setupItemTouchHelper();

        adapter.setOnItemChangeListener(new CategoryListMoveAdapter.OnItemChangeListener() {
            @Override
            public void setPosition(ArrayList<CategoryEntity> change_position_arr_category) {
                arr_change_category.clear();
                arr_change_category.addAll(change_position_arr_category);
            }
        });

        setupBackPressedCallback();

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateListPosition(arr_change_category);
            }
        });
    }

    private void setupItemTouchHelper() {
        ItemMoveCallback itemMoveCallback = new ItemMoveCallback(adapter);
        touchHelper = new ItemTouchHelper(itemMoveCallback);
        touchHelper.attachToRecyclerView(binding.rvCategory);
    }

    private void setupBackPressedCallback() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                updateListPosition(arr_change_category);
            }
        });
    }

    private void updateListPosition(ArrayList<CategoryEntity> change_position_arr_category) {
        for (int i = 0; i < change_position_arr_category.size(); i++) {
            change_position_arr_category.get(i).setPosition(i);
        }

        categoryViewModel.updateCategoryPositions(change_position_arr_category);

        navigateToDestination();
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
