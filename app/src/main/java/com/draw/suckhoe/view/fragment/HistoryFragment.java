package com.draw.suckhoe.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.draw.suckhoe.databinding.HistoryFragmentBinding;
import com.draw.suckhoe.factories.ViewModelFactory;
import com.draw.suckhoe.view.adapter.HistoryBPAdapter;
import com.draw.suckhoe.view.viewModels.BloodPressureViewModel;

public class HistoryFragment extends Fragment {

    HistoryFragmentBinding binding;
    HistoryBPAdapter adapter;
    BloodPressureViewModel viewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HistoryFragmentBinding.inflate(inflater, container, false);
        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(BloodPressureViewModel.class);

        displayRecycleAllHistory();
        return binding.getRoot();
    }

    private void displayRecycleAllHistory()
    {
        binding.recycleAllHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycleAllHistory.setHasFixedSize(true);

        viewModel.getListDataBPressure();
        viewModel.getBloodPressureListLiveData().observe(getViewLifecycleOwner(), list -> adapter.setList(list, list.size()));
        adapter = new HistoryBPAdapter();
        binding.recycleAllHistory.setAdapter(adapter);
    }
}
