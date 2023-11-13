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
import com.draw.suckhoe.model.BMIModel;
import com.draw.suckhoe.model.BloodGlucose;
import com.draw.suckhoe.model.BloodPressure;
import com.draw.suckhoe.myInterface.OnClickItemListener;
import com.draw.suckhoe.utils.MyConstants;
import com.draw.suckhoe.view.activity.DetailsActivity;
import com.draw.suckhoe.view.adapter.GenericAdapter;
import com.draw.suckhoe.view.viewModels.BMIViewModel;
import com.draw.suckhoe.view.viewModels.BloodGlucoseViewModel;
import com.draw.suckhoe.view.viewModels.BloodPressureViewModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment implements OnClickItemListener {

    HistoryFragmentBinding binding;
    GenericAdapter<BloodPressure> bloodPressureAdapter;
    GenericAdapter<BloodGlucose> bloodGlucoseAdapter;

    GenericAdapter<BMIModel> bmiModelAdapter;
    BloodPressureViewModel bloodPressureViewModel;
    BloodGlucoseViewModel bloodGlucoseViewModel;
    BMIViewModel bmiViewModel;

    List<?> historyList;

    private DetailsActivity activity;
    private int typeHistory = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HistoryFragmentBinding.inflate(inflater, container, false);
        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());

        historyList = new ArrayList<>();
        activity = (DetailsActivity) getActivity();

        binding.recycleAllHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycleAllHistory.setHasFixedSize(true);

        Bundle bundle = getArguments();
        if(bundle != null)
        {
            typeHistory = bundle.getInt("TYPE_HISTORY");
        }

        if(typeHistory == MyConstants.BLOOD_PRESSURE_ID)
        {
            bloodPressureViewModel = new ViewModelProvider(this, factory).get(BloodPressureViewModel.class);
            displayBPAllHistory();
        }else if(typeHistory == MyConstants.BLOOD_GLUCOSE_ID)
        {
            bloodGlucoseViewModel = new ViewModelProvider(this, factory).get(BloodGlucoseViewModel.class);
            displayBGAllHistory();
        }else if(typeHistory == MyConstants.BMI_ID)
        {
            bmiViewModel = new ViewModelProvider(this, factory).get(BMIViewModel.class);
            displayBMIAllHistory();
        }
        return binding.getRoot();
    }

    private void displayBPAllHistory()
    {
        bloodPressureViewModel.getListDataBPressure();
        bloodPressureViewModel.getBloodPressureListLiveData().observe(getViewLifecycleOwner(), list ->
        {
            bloodPressureAdapter.setList(list);
            historyList = list;

        });
        bloodPressureAdapter = new GenericAdapter<>();
        bloodPressureAdapter.setListener(this);
        binding.recycleAllHistory.setAdapter(bloodPressureAdapter);
    }

    private void displayBGAllHistory()
    {
        bloodGlucoseViewModel.getListDataGlucose();
        bloodGlucoseViewModel.getBloodGlucoseListLiveData().observe(getViewLifecycleOwner(), list ->
        {
            bloodGlucoseAdapter.setList(list);
            historyList = list;
        });
        bloodGlucoseAdapter = new GenericAdapter<>();
        bloodGlucoseAdapter.setListener(this);
        binding.recycleAllHistory.setAdapter(bloodGlucoseAdapter);
    }

    private void displayBMIAllHistory() {
        bmiViewModel.getListDataBMI();
        bmiViewModel.getListLiveData().observe(getViewLifecycleOwner(), list->
        {
            bmiModelAdapter.setList(list);
            historyList = list;
        });
        bmiModelAdapter = new GenericAdapter<>();
        bmiModelAdapter.setListener(this);
        binding.recycleAllHistory.setAdapter(bmiModelAdapter);
    }

    @Override
    public void onClickItemListener(int pos) {

        Bundle bundle = new Bundle();
        bundle.putInt("IS_NEW_DATA", 0);
        if(typeHistory == MyConstants.BLOOD_PRESSURE_ID)
        {
            BloodPressure bloodPressure = (BloodPressure) historyList.get(pos);
            bundle.putParcelable("bPressure_info", bloodPressure);
            BPSuccessFragment bpSuccessFragment = new BPSuccessFragment();
            bpSuccessFragment.setArguments(bundle);
            activity.replaceFragment(bpSuccessFragment);
        }else if(typeHistory == MyConstants.BLOOD_GLUCOSE_ID)
        {
            BloodGlucose bloodGlucose = (BloodGlucose) historyList.get(pos);
            bundle.putParcelable("bGlucose_info", bloodGlucose);
            BGSuccessFragment bgSuccessFragment = new BGSuccessFragment();
            bgSuccessFragment.setArguments(bundle);
            activity.replaceFragment(bgSuccessFragment);
        }else if(typeHistory == MyConstants.BMI_ID)
        {
            BMIModel bmiModel = (BMIModel) historyList.get(pos);
            bundle.putParcelable("bmi_info", bmiModel);
            BMISuccessFragment bmiSuccessFragment = new BMISuccessFragment();
            bmiSuccessFragment.setArguments(bundle);
            activity.replaceFragment(bmiSuccessFragment);
        }

    }
}
