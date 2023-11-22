package com.draw.suckhoe.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.draw.suckhoe.R;
import com.draw.suckhoe.databinding.WaterDetailFragmentBinding;
import com.draw.suckhoe.factories.ViewModelFactory;
import com.draw.suckhoe.view.adapter.WaterAdapter;
import com.draw.suckhoe.view.viewModels.WaterViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class WaterDetailFragment extends Fragment {

    WaterDetailFragmentBinding binding;
    WaterViewModel viewModel;

    WaterAdapter waterAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WaterDetailFragmentBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(WaterViewModel.class);

        String time = convertTimeToDate(MaterialDatePicker.todayInUtcMilliseconds());
        binding.tvDateRange.setText(String.format("%s - %s", time, time));
        observeWaterData(time, time);

        binding.constraintFilter.setOnClickListener(v->
        {
            MaterialDatePicker<Pair<Long, Long>> datePicker =
                    MaterialDatePicker.Builder.dateRangePicker()
                            .setTitleText("SELECT DATE RANGE")
                            .setTheme(R.style.ThemeMaterialCalendar)
                            .setSelection(Pair.create
                                    (MaterialDatePicker.todayInUtcMilliseconds(),
                                            MaterialDatePicker.todayInUtcMilliseconds()))
                            .build();

            datePicker.show(requireActivity().getSupportFragmentManager(), datePicker.toString());

            datePicker.addOnPositiveButtonClickListener(selection ->
            {
                String start =  convertTimeToDate(selection.first);
                String end = convertTimeToDate(selection.second);
                binding.tvDateRange.setText(String.format("%s - %s", start, end));
                observeWaterData(start, end);
            });

            datePicker.addOnNegativeButtonClickListener(v1 -> datePicker.dismiss());
        });

        return binding.getRoot();
    }

    private void observeWaterData(String startDate, String endDate)
    {
        viewModel.getListDataWater(startDate, endDate);
        viewModel.getListLiveData().observe(getViewLifecycleOwner(),
                waters -> waterAdapter.setList(waters));
        setDataAdapter();
    }

    private void setDataAdapter() {
        waterAdapter = new WaterAdapter();
        binding.historyDrinkWater.setAdapter(waterAdapter);
        binding.historyDrinkWater.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.historyDrinkWater.setHasFixedSize(true);
    }

    private String convertTimeToDate(Long time)
    {
        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utc.setTimeInMillis(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(utc.getTime());
    }
}
