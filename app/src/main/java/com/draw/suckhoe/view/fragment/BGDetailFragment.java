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
import com.draw.suckhoe.databinding.BgDetailFragmentBinding;
import com.draw.suckhoe.factories.ViewModelFactory;
import com.draw.suckhoe.model.BloodGlucose;
import com.draw.suckhoe.model.BloodPressure;
import com.draw.suckhoe.myInterface.OnClickItemListener;
import com.draw.suckhoe.utils.MyConstants;
import com.draw.suckhoe.view.activity.DetailsActivity;
import com.draw.suckhoe.view.adapter.GenericAdapter;
import com.draw.suckhoe.view.viewModels.BloodGlucoseViewModel;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BGDetailFragment extends Fragment implements OnClickItemListener {

    private BloodGlucoseViewModel viewModel;
    private BgDetailFragmentBinding binding;

    GenericAdapter<BloodGlucose> historyBGAdapter;

    private DetailsActivity activity;
    private List<BloodGlucose> bloodGlucoseList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BgDetailFragmentBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(BloodGlucoseViewModel.class);

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

    private void setUpBarChart() {
        BarDataSet barDataSet = new BarDataSet(new ArrayList<>(), "");
        BarData barData = new BarData(barDataSet);
        binding.barChartBGlucose.setData(barData);
        binding.barChartBGlucose.setScaleMinima(3f, 0f);
        binding.barChartBGlucose.setScaleEnabled(false);
        binding.barChartBGlucose.getAxisRight().setDrawLabels(false);
        binding.barChartBGlucose.getDescription().setEnabled(false);
        binding.barChartBGlucose.getLegend().setEnabled(false);
        binding.barChartBGlucose.setScrollContainer(true);
        binding.barChartBGlucose.invalidate();
        XAxis xAxis = binding.barChartBGlucose.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new ArrayList<>()));
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

    private void handleDataBarChart(List<BloodGlucose> listDataBG)
    {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> timeLabels = new ArrayList<>();
        String previousTime = "";
        for (int i = 0; i < listDataBG.size(); i++) {
            BloodGlucose bloodGlucose = listDataBG.get(i);
            float value = bloodGlucose.getLevelBGlucose();
            barEntries.add(new BarEntry(i, value));
            String currentTime = handleDateTime(bloodGlucose);
            if (previousTime.equals(currentTime))
                timeLabels.add("");
            else {
                previousTime = currentTime;
                timeLabels.add(currentTime);
            }
        }

        BarData barData = binding.barChartBGlucose.getData();
        if (barData != null) {
            barData.clearValues();
            barData.addDataSet(new BarDataSet(barEntries, ""));
            XAxis xAxis = binding.barChartBGlucose.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(timeLabels));
            xAxis.setLabelCount(timeLabels.size());
            xAxis.setAxisMinimum(0);
            xAxis.setAxisMaximum(timeLabels.size());
            binding.barChartBGlucose.setVisibleXRangeMinimum(10f);
            binding.barChartBGlucose.notifyDataSetChanged();
            binding.barChartBGlucose.invalidate();
        }
    }

    private String handleDateTime(BloodGlucose bloodGlucose) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM", Locale.US);
        try {
            Date date = inputDateFormat.parse(bloodGlucose.getTime());
            if (date != null)
                return outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void observeBGlucoseData() {
        viewModel.getListDataGlucose();
        viewModel.getBloodGlucoseListLiveData().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                handleDataBarChart(list);
                handlePieChart(list);
                List<BloodGlucose> latestData = new ArrayList<>();
                if(list.size() > 4)
                {
                    for(int i = list.size()-1; i >= list.size()-4; i--)
                    {
                        latestData.add(list.get(i));
                    }
                }else
                {
                    for(int i = list.size()-1; i >= 0; i--)
                    {
                        latestData.add(list.get(i));
                    }
                }
                bloodGlucoseList = latestData;
                historyBGAdapter.setList(latestData);
            }
        });
    }

    private void setDataAdapter() {
        historyBGAdapter = new GenericAdapter<>();
        historyBGAdapter.setListener(this);
        observeBGlucoseData();
        binding.recycleHistoryBG.setAdapter(historyBGAdapter);
        binding.recycleHistoryBG.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycleHistoryBG.setHasFixedSize(true);
    }

    private void onClickAllHistory() {
        binding.tvHistory.setOnClickListener(v -> {
            if(activity != null) {
                activity.setTitleName("Lịch sử");
                activity.findViewById(R.id.tvAddNewRecord).setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putInt("TYPE_HISTORY", MyConstants.BLOOD_GLUCOSE_ID);
                HistoryFragment historyFragment = new HistoryFragment();
                historyFragment.setArguments(bundle);
                activity.replaceFragment(historyFragment);
            }
        });
    }

    private void handlePieChart(List<BloodGlucose> listDataBG) {
        int countType1 = 0;
        int countType2 = 0;
        int countType3 = 0;
        int countType4 = 0;

        ArrayList<Integer> colors = new ArrayList<>();
        for (BloodGlucose bg : listDataBG) {
            int type = bg.getType();
            if (type == 1)
                countType1++;
            else if (type == 2)
                countType2++;
            else if (type == 3)
                countType3++;
            else if (type == 4)
                countType4++;
        }

        ArrayList<PieEntry> pieData = new ArrayList<>();
        if (countType1 != 0) {
            pieData.add(new PieEntry(countType1, getString(R.string.bGlucose_low)));
            colors.add(ContextCompat.getColor(requireContext(), R.color.blue_4th));
        }
        if (countType2 != 0) {
            colors.add(ContextCompat.getColor(requireContext(), R.color.green));
            pieData.add(new PieEntry(countType2, getString(R.string.bGlucose_normal)));
        }
        if (countType3 != 0) {
            colors.add(ContextCompat.getColor(requireContext(), R.color.yellow_primary));
            pieData.add(new PieEntry(countType3, getString(R.string.bGlucose_preDiabetes)));
        }
        if (countType4 != 0) {
            colors.add(ContextCompat.getColor(requireContext(), R.color.red_primary));
            pieData.add(new PieEntry(countType4, getString(R.string.bGlucose_diabetes)));
        }

        PieDataSet dataSet = new PieDataSet(pieData, "");

        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        PieData data = new PieData(dataSet);
        binding.pieChart.setData(data);
        binding.pieChart.setCenterText(listDataBG.size() + "\nIn total");
        binding.pieChart.invalidate();
    }

    @Override
    public void onClickItemListener(int pos) {
        BloodGlucose bloodGlucose = bloodGlucoseList.get(pos);
        Bundle bundle = new Bundle();
        bundle.putParcelable("bGlucose_info", bloodGlucose);
        bundle.putInt("IS_NEW_DATA", 0);
        BGSuccessFragment bgSuccessFragment = new BGSuccessFragment();
        bgSuccessFragment.setArguments(bundle);
        activity.replaceFragment(bgSuccessFragment);
    }
}
