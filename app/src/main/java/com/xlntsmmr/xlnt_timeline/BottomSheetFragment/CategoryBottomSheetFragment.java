package com.xlntsmmr.xlnt_timeline.BottomSheetFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.xlntsmmr.xlnt_timeline.R;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;

import java.util.UUID;

public class CategoryBottomSheetFragment extends BottomSheetDialogFragment {

    private EditText editTextCategoryName;
    private MaterialButton btn_close, btn_cancel, btn_add;

    String category_name;

    private OnCategoryNameSetListener mListener;

    public interface OnCategoryNameSetListener {
        void onCategoryNameSet(CategoryEntity categoryEntity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_bottom_sheet, container, false);

        editTextCategoryName = view.findViewById(R.id.editTextCategoryName);
//        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_add = view.findViewById(R.id.btn_add);
        btn_close = view.findViewById(R.id.btn_close);

//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editTextCategoryName.setText("");
//                category_name = "";
//                dismiss();
//            }
//        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_name = editTextCategoryName.getText().toString();

                if(!category_name.isEmpty()){
                    if (mListener != null) {
                        CategoryEntity categoryEntity = new CategoryEntity(UUID.randomUUID().toString(), "#000000", category_name);
                        mListener.onCategoryNameSet(categoryEntity);
                    }
                }

                dismiss();
            }
        });

        return view;
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void setOnCategoryNameSetListener(OnCategoryNameSetListener listener) {
        mListener = listener;
    }
}
