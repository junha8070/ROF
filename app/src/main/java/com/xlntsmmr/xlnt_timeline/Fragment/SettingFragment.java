//package com.xlntsmmr.xlnt_timeline.Fragment;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.navigation.Navigation;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.xlntsmmr.xlnt_timeline.R;
//import com.xlntsmmr.xlnt_timeline.databinding.FragmentSettingBinding;
//import com.xlntsmmr.xlnt_timeline.Entity.SettingEntity;
//import com.xlntsmmr.xlnt_timeline.ViewModel.SettingViewModel;
//
//public class SettingFragment extends Fragment {
//
//    String TAG = "SettingFragment";
//
//    private FragmentSettingBinding binding;
//    private SettingViewModel settingViewModel;
//    private boolean isDate_skip, isAlarm;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        settingViewModel = new ViewModelProvider(this).get(SettingViewModel.class);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        binding = FragmentSettingBinding.inflate(inflater, container, false);
//        View rootView = binding.getRoot();
//
//        settingViewModel.getAllSettings().observe(getViewLifecycleOwner(), new Observer<SettingEntity>() {
//            @Override
//            public void onChanged(SettingEntity setting) {
//                isDate_skip = setting.isDate_skip();
//                isAlarm = setting.isAlarm();
//
//                binding.switchDateSkip.setChecked(isDate_skip);
//                binding.switchAlarm.setChecked(isAlarm);
//            }
//        });
//
//        // 뒤로가기 버튼을 눌렀을 때 호출되는 메서드
//        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SettingEntity settingEntity = new SettingEntity();
//                settingEntity.setDate_skip(binding.switchDateSkip.isChecked());
//                settingEntity.setAlarm(binding.switchAlarm.isChecked());
//
//                Toast.makeText(getContext(), "슈정", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "스킵: "+binding.switchDateSkip.isChecked());
//                Log.d(TAG, "알람: "+binding.switchAlarm.isChecked());
//
//                // 데이터베이스에 업데이트된 설정 저장
//                settingViewModel.updateSetting(settingEntity);
//
//                Navigation.findNavController(requireView()).navigate(R.id.action_settingFragment_to_timeLineFragment);
//            }
//        });
//
//        return rootView;
//    }
//
//
//}