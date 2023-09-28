package com.draw.suckhoe.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.draw.suckhoe.R;
import com.draw.suckhoe.databinding.BpSuccessFragmentBinding;
import com.draw.suckhoe.model.BloodPressure;

public class BPSuccessFragment extends Fragment {

    BpSuccessFragmentBinding binding;
    BloodPressure bloodPressure;
    View clock, history;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BpSuccessFragmentBinding.inflate(inflater, container, false);
        if (getActivity() != null) {
            clock = getActivity().findViewById(R.id.timer);
            history = getActivity().findViewById(R.id.history);
            if (clock != null && history != null) {
                clock.setVisibility(View.GONE);
                history.setVisibility(View.GONE);
            }
        }
        getDataBPressureInfo();

        return binding.getRoot();
    }

    private void getDataBPressureInfo() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            bloodPressure = bundle.getParcelable("bPressure_info");
            if(bloodPressure != null)
            {
                binding.tvLvSystolic.setText(String.valueOf(bloodPressure.getSystolic()));
                binding.tvLvDiastolic.setText(String.valueOf(bloodPressure.getDiastolic()));
                binding.tvLvPulse.setText(String.valueOf(bloodPressure.getPulse()));
                binding.tvTime.setText(bloodPressure.getTime());
            }
        }

    }
}
