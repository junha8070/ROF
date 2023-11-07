package com.xlntsmmr.xlnt_timeline.BottomSheetFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.xlntsmmr.xlnt_timeline.ViewModel.CategoryViewModel;
import com.xlntsmmr.xlnt_timeline.databinding.FragmentShowCategoryBinding;

public class ShowCategoryBottomFragment extends BottomSheetDialogFragment {

    private FragmentShowCategoryBinding binding;
    String category_UUID, title;

    public ShowCategoryBottomFragment(String category_UUID, String title) {
        this.category_UUID = category_UUID;
        this.title = title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShowCategoryBinding.inflate(inflater, container, false);
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

        binding.editTextCategoryName.setText(title);

        binding.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 삭제 버튼 클릭 시
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(title+" 카테고리를 삭제할까요?").setMessage("카테고리에 속해있던 ROF도 삭제됩니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 사용자가 확인을 눌렀을 때 수행할 작업
                                deleteCategory(category_UUID);
                                dismiss(); // 다이얼로그 닫기
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 사용자가 취소를 눌렀을 때 수행할 작업
                                dialog.dismiss();
                            }
                        });
                // Create the AlertDialog object and return it
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 수정 버튼 클릭 시
                String newTitle = binding.editTextCategoryName.getText().toString();
                editCategory(category_UUID, newTitle);
                dismiss(); // 다이얼로그 닫기
            }
        });

        return rootView;
    }

    private void deleteCategory(String categoryUUID) {
        // CategoryRepository에서 해당 카테고리 삭제 메소드 호출
        CategoryViewModel categoryViewModel = new CategoryViewModel(requireActivity().getApplication());
        categoryViewModel.deleteCategory(categoryUUID);
    }

    private void editCategory(String categoryUUID, String newTitle) {
        // CategoryRepository에서 해당 카테고리 수정 메소드 호출
        CategoryViewModel categoryViewModel = new CategoryViewModel(requireActivity().getApplication());
        categoryViewModel.editCategory(categoryUUID, newTitle);
    }
}