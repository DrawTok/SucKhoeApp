package com.draw.suckhoe.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.draw.suckhoe.databinding.DashboardFragmentBinding;
import com.draw.suckhoe.utils.MyConstants;
import com.draw.suckhoe.view.activity.DetailsActivity;
import com.draw.suckhoe.view.viewModels.HomeViewModel;

public class DashBoardFragment extends Fragment {

    DashboardFragmentBinding binding;
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DashboardFragmentBinding.inflate(inflater, container, false);

        intent = new Intent(requireActivity(), DetailsActivity.class);
        binding.cardViewBloodPressure.setOnClickListener(v ->
            startDetailsActivityWithId(MyConstants.BLOOD_PRESSURE_ID));

        binding.cartViewBloodSugar.setOnClickListener(v ->
                startDetailsActivityWithId(MyConstants.BLOOD_GLUCOSE_ID));

        binding.cardViewBMI.setOnClickListener(v ->
                startDetailsActivityWithId(MyConstants.BMI_ID));

        binding.cardViewDrinkWater.setOnClickListener(v ->
                startDetailsActivityWithId(MyConstants.DRINK_WATER_ID));

        binding.cardViewCountStep.setOnClickListener(v ->
                startDetailsActivityWithId(MyConstants.COUNT_STEP_ID));

        return binding.getRoot();
    }

    private void startDetailsActivityWithId(int id) {
        intent.putExtra("idFragment", id);
        startActivity(intent);
    }
}
