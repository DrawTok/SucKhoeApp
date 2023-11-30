package com.draw.suckhoe.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.draw.suckhoe.R;
import com.draw.suckhoe.database.HealthDB;
import com.draw.suckhoe.databinding.BpSuccessFragmentBinding;
import com.draw.suckhoe.factories.ViewModelFactory;
import com.draw.suckhoe.model.BloodPressure;
import com.draw.suckhoe.utils.LevelResult;
import com.draw.suckhoe.utils.ViewColorRenderer;
import com.draw.suckhoe.view.viewModels.BloodPressureViewModel;

public class BPSuccessFragment extends Fragment {

    BpSuccessFragmentBinding binding;
    BloodPressure bloodPressure;

    private BloodPressureViewModel viewModel;
    HealthDB healthDB;

    View clock, tvDlt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BpSuccessFragmentBinding.inflate(inflater, container, false);

        healthDB = HealthDB.getInstance(requireContext());

        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(BloodPressureViewModel.class);

        if (getActivity() != null) {
            clock = getActivity().findViewById(R.id.timer);
            tvDlt = getActivity().findViewById(R.id.tvDelete);
            if (clock != null) {
                clock.setVisibility(View.GONE);
            }
        }

        getDataInBundle();

        tvDlt.setOnClickListener(v->
        {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
            tvDlt.setVisibility(View.GONE);
            clock.setVisibility(View.VISIBLE);
            viewModel.deleteBPressure(bloodPressure);
        });

        return binding.getRoot();
    }

    private void getDataInBundle() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            bloodPressure = bundle.getParcelable("bPressure_info");
            if(bloodPressure != null)
            {
                binding.tvLvValueF.setText(String.valueOf(bloodPressure.getSystolic()));
                binding.tvLvValueS.setText(String.valueOf(bloodPressure.getDiastolic()));
                binding.tvLvValueT.setText(String.valueOf(bloodPressure.getPulse()));
                binding.tvTime.setText(bloodPressure.getTime());
                renderColorView(bloodPressure);
            }

            if(bundle.getInt("IS_NEW_DATA") == 1)
                requireActivity().findViewById(R.id.tvFinish).setVisibility(View.VISIBLE);
            else
                tvDlt.setVisibility(View.VISIBLE);
        }

    }

    private void renderColorView(BloodPressure bloodPressure)
    {
        LevelResult result = new ViewColorRenderer().renderColorView(bloodPressure, requireContext());

        binding.tvNameLevel.setText(result.getNameRes());
        binding.tvAboutLevel.setText(result.getLevelRes());
        binding.viewColor.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(),result.getType()));
    }
}
