package com.draw.suckhoe.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.draw.suckhoe.databinding.RecordBmiFragmentBinding;

public class RecordBMIFragment extends Fragment {

    RecordBmiFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RecordBmiFragmentBinding.inflate(inflater, container, false);

        binding.imvEditWeight.setOnClickListener(v->
        {

        });

        return binding.getRoot();
    }
}
