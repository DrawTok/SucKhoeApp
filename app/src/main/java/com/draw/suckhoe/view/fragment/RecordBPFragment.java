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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.draw.suckhoe.database.HealthDB;
import com.draw.suckhoe.databinding.RecordBpFragmentBinding;
import com.draw.suckhoe.factories.ViewModelFactory;
import com.draw.suckhoe.model.BloodPressure;
import com.draw.suckhoe.model.LevelResult;
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
            BPSuccessFragment successFragment = new BPSuccessFragment();
            successFragment.setArguments(bundle);
            assert activity != null;
            activity.replaceFragment(successFragment);
        });


        return binding.getRoot();
    }

    private void displayLevelBPress() {
        LevelResult result = viewModel.getBPLevel();
        binding.tvNameLevel.setText(result.getNameRes());
        binding.tvLevel.setText(result.getLevelRes());
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
                bloodPressure = new BloodPressure(0, sys, dia, pulse, getTimeNow());
                handleVibrate();
                viewModel.setSelectSYS(sys);
                viewModel.setSelectDIA(dia);
                viewModel.setSelectPulse(pulse);
                displayLevelBPress();
            }
        });
    }

    private String getTimeNow()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
