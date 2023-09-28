package com.draw.suckhoe.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.draw.suckhoe.databinding.DashboardFragmentBinding;
import com.draw.suckhoe.view.activity.DetailsActivity;
import com.draw.suckhoe.view.viewModels.HomeViewModel;

public class DashBoardFragment extends Fragment {

    DashboardFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DashboardFragmentBinding.inflate(inflater, container, false);
        HomeViewModel homeViewModel = new HomeViewModel('1');
        homeViewModel.getIdFragment().observe(requireActivity(), id -> {
            Intent intent = new Intent(requireActivity(), DetailsActivity.class);
            intent.putExtra("idFragment", id);
            startActivity(intent);
        });
        binding.setHomeViewModel(homeViewModel);
        return binding.getRoot();
    }
}
