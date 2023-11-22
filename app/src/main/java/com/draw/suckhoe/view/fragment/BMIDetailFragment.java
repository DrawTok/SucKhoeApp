package com.draw.suckhoe.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.draw.suckhoe.R;
import com.draw.suckhoe.databinding.BmiDetailsFragmentBinding;
import com.draw.suckhoe.factories.ViewModelFactory;
import com.draw.suckhoe.model.BMIModel;
import com.draw.suckhoe.myInterface.OnClickItemListener;
import com.draw.suckhoe.utils.MyConstants;
import com.draw.suckhoe.view.activity.DetailsActivity;
import com.draw.suckhoe.view.adapter.GenericAdapter;
import com.draw.suckhoe.view.viewModels.BMIViewModel;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BMIDetailFragment extends Fragment implements OnClickItemListener {
    BmiDetailsFragmentBinding binding;
    BMIViewModel viewModel;

    GenericAdapter<BMIModel> historyAdapter;

    private DetailsActivity activity;

    private List<BMIModel> bmiList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BmiDetailsFragmentBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(BMIViewModel.class);

        activity = (DetailsActivity) getActivity();

        setUpUI();

        return binding.getRoot();
    }

    private void setUpUI() {
        setDataAdapter();
        setUpBarChart();
        setupPieChart();
        onClickAllHistory();
    }

    private void setupPieChart() {
        binding.pieChart.getDescription().setEnabled(false);
        binding.pieChart.setCenterTextSize(20f);
        binding.pieChart.setDrawEntryLabels(false);
        binding.pieChart.setHoleRadius(40f);
        binding.pieChart.setTransparentCircleRadius(45f);
        binding.pieChart.setTouchEnabled(false);
        binding.pieChart.getLegend().setEnabled(false);
        binding.pieChart.invalidate();
    }

    private void setUpBarChart() {
        BarDataSet barDataSet = new BarDataSet(new ArrayList<>(), "");
        BarData barData = new BarData(barDataSet);
        binding.barChartBMI.setData(barData);
        binding.barChartBMI.setScaleMinima(3f, 0f);
        binding.barChartBMI.setScaleEnabled(false);
        binding.barChartBMI.getAxisRight().setDrawLabels(false);
        binding.barChartBMI.getDescription().setEnabled(false);
        binding.barChartBMI.getLegend().setEnabled(false);
        binding.barChartBMI.setScrollContainer(true);
        binding.barChartBMI.invalidate();
        XAxis xAxis = binding.barChartBMI.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new ArrayList<>()));
    }

    private void setDataAdapter() {
        historyAdapter = new GenericAdapter<>();
        historyAdapter.setListener(this);
        observeBMIData();
        binding.recycleHistoryBMI.setAdapter(historyAdapter);
        binding.recycleHistoryBMI.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycleHistoryBMI.setHasFixedSize(true);
    }

    private void observeBMIData() {
        viewModel.getListDataBMI();
        viewModel.getListLiveData().observe(getViewLifecycleOwner(), this::updateUI);
    }

    private void updateUI(List<BMIModel> bmiModels) {
        if (bmiModels != null) {
            handleDataBarChart(bmiModels);
            handlePieChart(bmiModels);
            binding.tvValueMax.setText(String.valueOf(findMax(bmiModels)));
            binding.tvValueMin.setText(String.valueOf(findMin(bmiModels)));
            binding.tvValueAvg.setText(String.valueOf(calcAvgBMI(bmiModels)));
            bmiList = getLatestData(bmiModels);
            historyAdapter.setList(bmiList);
        }
    }

    private List<BMIModel> getLatestData(List<BMIModel> bmiModels) {
        int numToRetrieve = Math.min(bmiModels.size(), 4);
        List<BMIModel> latestData = new ArrayList<>(bmiModels.subList(bmiModels.size() - numToRetrieve, bmiModels.size()));
        Collections.reverse(latestData);
        return latestData;
    }


    private void handlePieChart(List<BMIModel> bmiModels) {
        int countType1 = 0;
        int countType2 = 0;
        int countType3 = 0;
        int countType4 = 0;
        int countType5 = 0;
        int countType6 = 0;
        int countType7 = 0;
        int countType8 = 0;

        ArrayList<Integer> colors = new ArrayList<>();
        for (BMIModel bmiModel : bmiModels) {
            int type = bmiModel.getType();
            if (type == 1)
                countType1++;
            else if (type == 2)
                countType2++;
            else if (type == 3)
                countType3++;
            else if (type == 4)
                countType4++;
            else if (type == 5)
                countType5++;
            else if (type == 6)
                countType6++;
            else if (type == 7)
                countType7++;
            else if (type == 8)
                countType8++;
        }

        ArrayList<PieEntry> pieData = new ArrayList<>();
        if (countType1 != 0) {
            pieData.add(new PieEntry(countType1, getString(R.string.bmi_lowest)));
            colors.add(ContextCompat.getColor(requireContext(), R.color.purple));
        }
        if (countType2 != 0) {
            colors.add(ContextCompat.getColor(requireContext(), R.color.blue_5th));
            pieData.add(new PieEntry(countType2, getString(R.string.bmi_lower)));
        }
        if (countType3 != 0) {
            colors.add(ContextCompat.getColor(requireContext(), R.color.blue_4th));
            pieData.add(new PieEntry(countType3, getString(R.string.bmi_low)));
        }
        if (countType4 != 0) {
            colors.add(ContextCompat.getColor(requireContext(), R.color.green));
            pieData.add(new PieEntry(countType4, getString(R.string.bmi_normal)));
        }
        if (countType5 != 0) {
            colors.add(ContextCompat.getColor(requireContext(), R.color.yellow_primary));
            pieData.add(new PieEntry(countType5, getString(R.string.bmi_high)));
        }
        if (countType6 != 0) {
            colors.add(ContextCompat.getColor(requireContext(), R.color.orange_primary));
            pieData.add(new PieEntry(countType6, getString(R.string.bmi_stage_1)));
        }
        if (countType7 != 0) {
            colors.add(ContextCompat.getColor(requireContext(), R.color.orange_secondary));
            pieData.add(new PieEntry(countType7, getString(R.string.bmi_stage_2)));
        }
        if (countType8 != 0) {
            colors.add(ContextCompat.getColor(requireContext(), R.color.red_primary));
            pieData.add(new PieEntry(countType8, getString(R.string.bmi_stage_3)));
        }


        PieDataSet dataSet = new PieDataSet(pieData, "");

        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        PieData data = new PieData(dataSet);
        binding.pieChart.setData(data);
        binding.pieChart.setCenterText(bmiModels.size() + "\nIn total");
        binding.pieChart.invalidate();
    }

    private void handleDataBarChart(List<BMIModel> bmiModels) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> timeLabels = new ArrayList<>();
        String previousTime = "";
        for (int i = 0; i < bmiModels.size(); i++) {
            BMIModel bmiModel = bmiModels.get(i);
            float value = bmiModel.getBmi();
            barEntries.add(new BarEntry(i, value));
            String currentTime = handleDateTime(bmiModel);
            if (previousTime.equals(currentTime))
                timeLabels.add("");
            else {
                previousTime = currentTime;
                timeLabels.add(currentTime);
            }
        }

        BarData barData = binding.barChartBMI.getData();
        if (barData != null) {
            barData.clearValues();
            barData.addDataSet(new BarDataSet(barEntries, ""));
            XAxis xAxis = binding.barChartBMI.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(timeLabels));
            xAxis.setLabelCount(timeLabels.size());
            xAxis.setAxisMinimum(0);
            xAxis.setAxisMaximum(timeLabels.size());
            binding.barChartBMI.setVisibleXRangeMinimum(10f);
            binding.barChartBMI.notifyDataSetChanged();
            binding.barChartBMI.invalidate();
        }
    }

    private String handleDateTime(BMIModel bmiModel) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM", Locale.US);
        try {
            Date date = inputDateFormat.parse(bmiModel.getTime());
            if (date != null)
                return outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void onClickAllHistory() {
        binding.tvHistory.setOnClickListener(v -> {
            if(activity != null) {
                activity.setTitleName("Lịch sử");
                activity.findViewById(R.id.tvAddNewRecord).setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putInt("TYPE_HISTORY", MyConstants.BMI_ID);
                HistoryFragment historyFragment = new HistoryFragment();
                historyFragment.setArguments(bundle);
                activity.replaceFragment(historyFragment);
            }
        });
    }

    @Override
    public void onClickItemListener(int pos) {
        BMIModel bmiModel = bmiList.get(pos);
        Bundle bundle = new Bundle();
        bundle.putParcelable("bmi_info", bmiModel);
        bundle.putInt("IS_NEW_DATA", 0);
        BMISuccessFragment bmiSuccessFragment = new BMISuccessFragment();
        bmiSuccessFragment.setArguments(bundle);
        activity.replaceFragment(bmiSuccessFragment);
    }

    private float findMax(List<BMIModel> bmiModels)
    {
        if(!bmiModels.isEmpty())
        {
            float maxBMI = Float.MIN_VALUE;
            for (BMIModel bmiModel : bmiModels)
            {
                maxBMI = Math.max(bmiModel.getBmi(), maxBMI);
            }
            return maxBMI;
        }
        return 0;
    }

    private float findMin(List<BMIModel> bmiModels)
    {
        if(!bmiModels.isEmpty())
        {
            float minBMI = Float.MAX_VALUE;
            for (BMIModel bmiModel : bmiModels)
            {
                minBMI = Math.min(bmiModel.getBmi(), minBMI);
            }
            return minBMI;
        }
        return 0;
    }

    private float calcAvgBMI(List<BMIModel> bmiModels)
    {
        if(!bmiModels.isEmpty())
        {
            float totalBMI = 0;
            for (BMIModel bmiModel : bmiModels)
            {
                totalBMI += bmiModel.getBmi();
            }
            return Math.round((totalBMI / bmiModels.size()) * 10.0f) / 10.0f;
        }
        return 0;
    }
}
