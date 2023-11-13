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
import com.draw.suckhoe.model.BMIModel;
import com.draw.suckhoe.utils.LevelResult;
import com.draw.suckhoe.utils.ViewColorRenderer;
import com.draw.suckhoe.view.viewModels.BMIViewModel;

public class BMISuccessFragment extends Fragment {

    BpSuccessFragmentBinding binding;

    private BMIViewModel viewModel;
    private BMIModel bmiModel;
    HealthDB healthDB;
    View clock, tvDlt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = BpSuccessFragmentBinding.inflate(inflater, container, false);

        healthDB = HealthDB.getInstance(requireContext());

        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(BMIViewModel.class);

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
            viewModel.deleteDataBMI(bmiModel);
        });

        return binding.getRoot();
    }

    private void getDataInBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            bmiModel = bundle.getParcelable("bmi_info");
            if (bmiModel != null) {
                float bmi = bmiModel.getBmi();

                binding.nameValueF.setText("Cân nặng");
                binding.tvLvValueF.setText(String.valueOf(bmiModel.getWeight()));
                binding.tvUnitValueF.setText("Kg");

                binding.nameValueS.setText("Chiều cao");
                binding.tvLvValueS.setText(String.valueOf(bmiModel.getHeight()));
                binding.tvUnitValueS.setText("Cm");

                binding.nameValueT.setText("BMI");
                binding.tvLvValueT.setText(String.valueOf(bmi));
                binding.tvUnitValueT.setText("");

                binding.tvTime.setText(bmiModel.getTime());
                renderColorView(bmiModel);
            }

            if (bundle.getInt("IS_NEW_DATA") == 1)
                requireActivity().findViewById(R.id.tvFinish).setVisibility(View.VISIBLE);
            else
                tvDlt.setVisibility(View.VISIBLE);
        }
    }
    private void renderColorView(BMIModel bmiModel)
    {
        LevelResult result = new ViewColorRenderer().renderColorView(bmiModel, requireContext());

        binding.tvNameLevel.setText(result.getNameRes());
        binding.tvAboutLevel.setText(result.getLevelRes());
        binding.viewColor.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(),result.getType()));
    }
}
