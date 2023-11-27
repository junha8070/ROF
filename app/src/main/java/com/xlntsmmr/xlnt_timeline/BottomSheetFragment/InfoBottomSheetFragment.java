package com.xlntsmmr.xlnt_timeline.BottomSheetFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.xlntsmmr.xlnt_timeline.databinding.BottomSheetFragmentDialogInfoBinding;

public class InfoBottomSheetFragment extends BottomSheetDialogFragment {

    BottomSheetFragmentDialogInfoBinding binding;

    String currentVersion;
    boolean forceUpdate;
    String latestVersion;
    String minVersion;
    String updateNews;
    String newFunction;

    public InfoBottomSheetFragment(String currentVersion, boolean forceUpdate, String latestVersion, String minVersion, String updateNews, String newFunction) {
        this.currentVersion = currentVersion;
        this.forceUpdate = forceUpdate;
        this.latestVersion = latestVersion;
        this.minVersion = minVersion;
        this.updateNews = updateNews;
        this.newFunction = newFunction;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = BottomSheetFragmentDialogInfoBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        if (forceUpdate && !currentVersion.equals(latestVersion)) {
            binding.btnClose.setVisibility(View.INVISIBLE);
        }

        binding.tvContent.setText(String.format("%s\n%s", updateNews, newFunction));
        binding.tvNowVer.setText(currentVersion);
        binding.tvLatestVer.setText(latestVersion);

        if (currentVersion.equals(latestVersion) || latestVersion.equals("버전 확인 중")) {
            binding.btnUpdate.setVisibility(View.GONE);
        }

        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
                marketLaunch.setData(Uri.parse("market://details?id=" + getActivity().getPackageName()));
                startActivity(marketLaunch);
            }
        });

        return rootView;
    }
}