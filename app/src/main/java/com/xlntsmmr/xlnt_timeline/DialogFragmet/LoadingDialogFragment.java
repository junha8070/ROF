package com.xlntsmmr.xlnt_timeline.DialogFragmet;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.xlntsmmr.xlnt_timeline.R;

public class LoadingDialogFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext(), R.style.LoadingDialogStyle); // Custom style for the dialog
        dialog.setContentView(R.layout.fragment_loading); // Layout with LottieAnimationView

        return dialog;
    }
}