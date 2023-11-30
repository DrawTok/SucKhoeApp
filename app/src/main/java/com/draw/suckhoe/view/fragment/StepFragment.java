package com.draw.suckhoe.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.draw.suckhoe.R;
import com.draw.suckhoe.databinding.StepFragmentBinding;
import com.draw.suckhoe.helper.StepCounterService;

public class StepFragment extends Fragment {

    StepFragmentBinding binding;

    BroadcastReceiver stepCountReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = StepFragmentBinding.inflate(inflater, container, false);

        if (getActivity() != null) {
            getActivity().findViewById(R.id.tvAddNewRecord).setVisibility(View.GONE);
        }

        requireContext().startService(new Intent(requireActivity(), StepCounterService.class));
        stepCountReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int stepCount = intent.getIntExtra("count", 0);
                binding.progressStep.setProgress(stepCount);
                binding.tvCount.setText(String.valueOf(stepCount));
            }
        };

        requireContext().registerReceiver(stepCountReceiver, new IntentFilter("stepCount"));

        return binding.getRoot();
    }
}
