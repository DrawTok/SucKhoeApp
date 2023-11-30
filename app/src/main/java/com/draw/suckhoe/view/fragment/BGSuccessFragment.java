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
import com.draw.suckhoe.databinding.BgSuccessFragmentBinding;
import com.draw.suckhoe.factories.ViewModelFactory;
import com.draw.suckhoe.model.BloodGlucose;
import com.draw.suckhoe.utils.LevelResult;
import com.draw.suckhoe.utils.ViewColorRenderer;
import com.draw.suckhoe.view.viewModels.BloodGlucoseViewModel;

public class BGSuccessFragment extends Fragment {

    BgSuccessFragmentBinding binding;
    View clock, tvDlt;
    private BloodGlucoseViewModel viewModel;
    HealthDB healthDB;
    private BloodGlucose bloodGlucose;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = BgSuccessFragmentBinding.inflate(inflater, container, false);

        healthDB = HealthDB.getInstance(requireContext());

        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(BloodGlucoseViewModel.class);

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
            viewModel.deleteBGlucose(bloodGlucose);
        });

        return binding.getRoot();
    }

    private void getDataInBundle() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            bloodGlucose = bundle.getParcelable("bGlucose_info");
            if(bloodGlucose != null)
            {
                binding.tvLevel.setText(String.valueOf(bloodGlucose.getLevelBGlucose()));
                renderColorView(bloodGlucose);
            }
            if(bundle.getInt("IS_NEW_DATA") == 1)
                requireActivity().findViewById(R.id.tvFinish).setVisibility(View.VISIBLE);
            else
                tvDlt.setVisibility(View.VISIBLE);
        }
    }

    private void renderColorView(BloodGlucose bloodGlucose)
    {
        LevelResult levelResult = new ViewColorRenderer().renderColorView(bloodGlucose, requireContext());
        binding.tvNameLevel.setText(levelResult.getNameRes());
        binding.tvLevelSmall.setText(levelResult.getLevelRes());
        binding.viewColor.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), levelResult.getType()));
    }
}
