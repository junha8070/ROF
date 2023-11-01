package com.xlntsmmr.xlnt_timeline.BottomSheetFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.xlntsmmr.xlnt_timeline.R;
import com.xlntsmmr.xlnt_timeline.databinding.FragmentAddBottomSheetBinding;
import com.xlntsmmr.xlnt_timeline.databinding.FragmentInfoBottomSheetBinding;

public class InfoBottomSheetFragment extends BottomSheetDialogFragment {

    FragmentInfoBottomSheetBinding binding;

    String currentVersion;
    boolean forceUpdate;
    String latestVersion;
    String minVersion;
//    String updateNewsJson;
    String jsonLatestVersion;
    String jsonUpdateNews;
    String jsonNewFunction;

    public InfoBottomSheetFragment(String currentVersion, boolean forceUpdate, String latestVersion, String minVersion, String jsonLatestVersion, String jsonUpdateNews, String jsonNewFunction) {
        this.currentVersion = currentVersion;
        this.forceUpdate = forceUpdate;
        this.latestVersion = latestVersion;
        this.minVersion = minVersion;
        this.jsonLatestVersion = jsonLatestVersion;
        this.jsonUpdateNews = jsonUpdateNews;
        this.jsonNewFunction = jsonNewFunction;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInfoBottomSheetBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();


        binding.tvContent.setText(String.format("%s\n%s", jsonUpdateNews, jsonNewFunction));
        binding.tvNowVer.setText(currentVersion);
        binding.tvLatestVer.setText(latestVersion);

        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }
}