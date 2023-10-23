package com.draw.suckhoe.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.draw.suckhoe.R;
import com.draw.suckhoe.database.HealthDB;
import com.draw.suckhoe.databinding.RecordBpFragmentBinding;
import com.draw.suckhoe.factories.ViewModelFactory;
import com.draw.suckhoe.model.BloodPressure;
import com.draw.suckhoe.utils.LevelResult;
import com.draw.suckhoe.view.activity.DetailsActivity;
import com.draw.suckhoe.view.viewModels.BloodPressureViewModel;
import com.shawnlin.numberpicker.NumberPicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class RecordBPFragment extends Fragment {

    private Vibrator vibrator;
    private RecordBpFragmentBinding binding;
    BloodPressure bloodPressure;
    private BloodPressureViewModel viewModel;
    HealthDB healthDB;

    private LevelResult levelResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RecordBpFragmentBinding.inflate(inflater, container, false);

        DetailsActivity activity= (DetailsActivity) getActivity();

        //rung
        vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

        //generate database
        healthDB = HealthDB.getInstance(requireContext());
        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(BloodPressureViewModel.class);

        setScrollListener(binding.wheelSystolic);
        setScrollListener(binding.wheelDiastolic);
        setScrollListener(binding.wheelPulse);

        viewModel.getSelectSYS().observe(getViewLifecycleOwner(), integer -> {});
        viewModel.getSelectDIA().observe(getViewLifecycleOwner(), integer -> {});
        viewModel.getSelectPulse().observe(getViewLifecycleOwner(), integer -> {});

        binding.tvConfirm.setOnClickListener(view ->
        {
            viewModel.insertBPressure(bloodPressure);
            Bundle bundle = new Bundle();
            bundle.putParcelable("bPressure_info", bloodPressure);
            bundle.putInt("IS_NEW_DATA", 1);
            BPSuccessFragment successFragment = new BPSuccessFragment();
            successFragment.setArguments(bundle);
            if(activity != null)
                activity.replaceFragment(successFragment);
        });


        return binding.getRoot();
    }

    private void displayLevelBPress(LevelResult levelResult) {
        binding.tvNameLevel.setText(levelResult.getNameRes());
        binding.tvLevel.setText(levelResult.getLevelRes());

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.imgArrow.getLayoutParams();

        int viewId;

        switch (levelResult.getType()) {
            case 1:
                viewId = R.id.viewColorLow;
                break;
            case 2:
                viewId = R.id.viewColorNormal;
                break;
            case 3:
                viewId = R.id.viewColorHigh;
                break;
            case 4:
                viewId = R.id.viewColorStage1;
                break;
            case 5:
                viewId = R.id.viewColorStage2;
                break;
            default:
                viewId = R.id.viewColorStage3;
                break;
        }

        layoutParams.startToStart = viewId;
        layoutParams.endToEnd = viewId;

        binding.imgArrow.setLayoutParams(layoutParams);
    }


    private void handleVibrate()
    {
        if(vibrator.hasVibrator()) {
            VibrationEffect vibrationEffect = VibrationEffect.createOneShot
                    (100, VibrationEffect.DEFAULT_AMPLITUDE);
            vibrator.vibrate(vibrationEffect);
        }
    }

    private void setScrollListener(NumberPicker numberPicker)
    {
        numberPicker.setOnScrollListener((view, scrollState) -> {
            int sys = binding.wheelSystolic.getValue();
            int dia = binding.wheelDiastolic.getValue();
            int pulse = binding.wheelPulse.getValue();
            if(scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE)
            {
                viewModel.getLevelResultLiveData().observe(getViewLifecycleOwner(), result -> {
                    if (result != null) {
                        levelResult = result;
                        bloodPressure = new BloodPressure(0, sys, dia, pulse, getTimeNow(), result.getType());
                        displayLevelBPress(levelResult);
                    }
                });
                viewModel.setSelectSYS(sys);
                viewModel.setSelectDIA(dia);
                viewModel.setSelectPulse(pulse);
                handleVibrate();
            }
        });
    }

    private String getTimeNow()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
