package com.xlntsmmr.xlnt_timeline.BottomSheetFragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.xlntsmmr.xlnt_timeline.R;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.databinding.BottomSheetFragmentDialogAddCategoryBinding;
import com.xlntsmmr.xlnt_timeline.databinding.BottomSheetFragmentDialogAddRofBinding;

import java.util.UUID;

public class CategoryBottomSheetFragment extends BottomSheetDialogFragment {

    private BottomSheetFragmentDialogAddCategoryBinding binding;

    String category_name;

    private OnCategoryNameSetListener mListener;

    public interface OnCategoryNameSetListener {
        void onCategoryNameSet(CategoryEntity categoryEntity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = BottomSheetFragmentDialogAddCategoryBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

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

                if(!category_name.isEmpty()){
                    if (mListener != null) {
                        CategoryEntity categoryEntity = new CategoryEntity(UUID.randomUUID().toString(), "#000000", category_name);
                        mListener.onCategoryNameSet(categoryEntity);
                    }
                }

                dismiss();
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
}
