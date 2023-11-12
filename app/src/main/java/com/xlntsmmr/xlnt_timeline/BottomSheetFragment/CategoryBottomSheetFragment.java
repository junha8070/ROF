package com.xlntsmmr.xlnt_timeline.BottomSheetFragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.xlntsmmr.xlnt_timeline.DialogFragmet.LoadingDialogFragment;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.ViewModel.CategoryViewModel;
import com.xlntsmmr.xlnt_timeline.databinding.BottomSheetFragmentDialogAddCategoryBinding;
import com.xlntsmmr.xlnt_timeline.databinding.BottomSheetFragmentDialogAddRofBinding;

import java.util.UUID;

public class CategoryBottomSheetFragment extends BottomSheetDialogFragment {

    private BottomSheetFragmentDialogAddCategoryBinding binding;

    String category_name;

    private OnCategoryNameSetListener mListener;
    private CategoryViewModel categoryViewModel;
    private LoadingDialogFragment loadingDialog;

    public interface OnCategoryNameSetListener {
        void onCategoryNameSet(CategoryEntity categoryEntity);
    }

    int position = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = BottomSheetFragmentDialogAddCategoryBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        binding.editTextCategoryName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.editTextCategoryName.getWindowToken(), 0);    //hide keyboard
                    return true;
                }
                return false;
            }
        });

        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_name = binding.editTextCategoryName.getText().toString();
//                showLoadingDialog();

                if (!category_name.isEmpty()&&!category_name.trim().isEmpty()) {
                    categoryViewModel.getCategoryCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer size) {
                            showLoadingDialog();
                            if(size!=null){
                                position = size;
                                CategoryEntity categoryEntity = new CategoryEntity(UUID.randomUUID().toString(), "#000000", category_name, position);
                                mListener.onCategoryNameSet(categoryEntity);
                                dismissLoadingDialog();
                            }
                        }
                    });
                    dismiss();
                }else{
                    binding.editTextCategoryNameLayout.setError("카테고리 이름을 입력해주세요.");
                }
            }
        });

        return rootView;
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void setOnCategoryNameSetListener(OnCategoryNameSetListener listener) {
        mListener = listener;
    }

    private void showLoadingDialog() {
        loadingDialog = new LoadingDialogFragment();
        loadingDialog.show(getChildFragmentManager(), "loading_dialog");
    }

    private void dismissLoadingDialog() {
        loadingDialog.dismiss();
//        if(loadingDialog==null){
//            Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
//        }
//        if (loadingDialog != null) {
//            loadingDialog.dismiss();
//        }
    }
}
