package com.draw.suckhoe.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.chingtech.rulerview.library.RulerView;
import com.draw.suckhoe.R;
import com.draw.suckhoe.database.HealthDB;
import com.draw.suckhoe.databinding.RecordBmiFragmentBinding;
import com.draw.suckhoe.factories.ViewModelFactory;
import com.draw.suckhoe.model.BMIModel;
import com.draw.suckhoe.view.activity.DetailsActivity;
import com.draw.suckhoe.view.viewModels.BMIViewModel;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordBMIFragment extends Fragment {

    RecordBmiFragmentBinding binding;
    Vibrator vibrator;
    private float preValue = 0.0f;
    HealthDB healthDB;

    BMIViewModel viewModel;
    int type = 0;
    Dialog dialog;
    private boolean isSuccess = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RecordBmiFragmentBinding.inflate(inflater, container, false);

        DetailsActivity activity= (DetailsActivity) getActivity();

        healthDB = HealthDB.getInstance(requireContext());

        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(BMIViewModel.class);

        vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

        binding.imvEditWeight.setOnClickListener(v->
                showDialog("Cân nặng", "Kg", Float.parseFloat(binding.tvValueWeight.getText().toString())));

        binding.imvEditHeight.setOnClickListener(v->
                showDialog("Chiều cao", "Cm", Float.parseFloat(binding.tvValueHeight.getText().toString())));

        binding.tvConfirm.setOnClickListener(v->
        {
            float weight = Float.parseFloat(binding.tvValueWeight.getText().toString());
            float height = Float.parseFloat(binding.tvValueHeight.getText().toString());
            float valueBMI = (float) (weight / (Math.pow((height / 100), 2)));
            valueBMI = Math.round(valueBMI * 10.0f) / 10.0f;
            if(type != 0)
            {
                BMIModel bmiModel = new BMIModel(0, weight, height, valueBMI, getTimeNow(), type);
                viewModel.insertDataBMI(bmiModel);
                new Handler().postDelayed(() -> {
                    if(isSuccess){
                        displayOverlaySuccess();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("bmi_info", bmiModel);
                        bundle.putInt("IS_NEW_DATA", 1);
                        BMISuccessFragment bmiSuccessFragment = new BMISuccessFragment();
                        bmiSuccessFragment.setArguments(bundle);
                        new Handler().postDelayed(() -> {
                            dialog.dismiss();
                            if(activity != null)
                                activity.replaceFragment(bmiSuccessFragment);
                        }, 470);
                    }else
                    {
                        Toast.makeText(activity, R.string.new_add_error, Toast.LENGTH_SHORT).show();
                    }
                }, 100);
            }
        });

        viewModel.getIsSuccess().observe(getViewLifecycleOwner(), isResult -> isSuccess = isResult);

        return binding.getRoot();
    }

    private String getTimeNow()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void showDialog(String nameDialog, String unit,float defaultValue) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_hw_bmi);
        if(dialog.getWindow() != null)
        {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }
        TextView tvNameDialog = dialog.findViewById(R.id.tvNameDialog);
        TextView tvUnit = dialog.findViewById(R.id.tvUnitOfRuler);
        RulerView rulerView = dialog.findViewById(R.id.ruler);
        MaterialButton button = dialog.findViewById(R.id.btnConfirm);
        tvNameDialog.setText(nameDialog);
        tvUnit.setText(unit);
        rulerView.initViewParam(defaultValue, 0, 250f, 0.1f);

        rulerView.setChooseValueChangeListener(v -> {
            if(Math.abs(v-preValue) > 0) {
                handleVibrator();
                preValue = v;
            }
        });

        button.setOnClickListener(v->
        {
            if(nameDialog.equals("Cân nặng"))
                binding.tvValueWeight.setText(String.valueOf(preValue));
            else
                binding.tvValueHeight.setText(String.valueOf(preValue));

            calcBMI();

            dialog.dismiss();
        });

        dialog.show();
    }

    private void handleVibrator() {
        if(vibrator.hasVibrator())
        {
            VibrationEffect effect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE);
            vibrator.vibrate(effect);
        }
    }

    private void calcBMI()
    {
        float weight = Float.parseFloat(binding.tvValueWeight.getText().toString());
        float height = Float.parseFloat(binding.tvValueHeight.getText().toString());
        float valueBMI = (float) (weight / (Math.pow((height / 100), 2)));
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.imgArrow.getLayoutParams();

        int viewId;
        String nameLevelBMI, levelBMI;
        if(valueBMI < 16f) {
            viewId = R.id.viewColorLowest;
            nameLevelBMI = getString(R.string.bmi_lowest);
            levelBMI = getString(R.string.bmi_level_lowest);
            type = 1;
        }
        else if(valueBMI < 17f) {
            viewId = R.id.viewColorLower;
            nameLevelBMI = getString(R.string.bmi_lower);
            levelBMI = getString(R.string.bmi_level_lower);
            type = 2;
        }
        else if(valueBMI < 18.5) {
            viewId = R.id.viewColorLow;
            nameLevelBMI = getString(R.string.bmi_low);
            levelBMI = getString(R.string.bmi_level_low);
            type = 3;
        }
        else if(valueBMI < 25) {
            viewId = R.id.viewColorNormal;
            nameLevelBMI = getString(R.string.bmi_normal);
            levelBMI = getString(R.string.bmi_level_normal);
            type = 4;
        }
        else if(valueBMI < 30) {
            viewId = R.id.viewColorHigh;
            nameLevelBMI = getString(R.string.bmi_high);
            levelBMI = getString(R.string.bmi_level_high);
            type = 5;
        }
        else if(valueBMI < 35) {
            viewId = R.id.viewColorStage1;
            nameLevelBMI = getString(R.string.bmi_stage_1);
            levelBMI = getString(R.string.bmi_level_stage1);
            type = 6;
        }
        else if(valueBMI < 40) {
            viewId = R.id.viewColorStage2;
            nameLevelBMI = getString(R.string.bmi_stage_2);
            levelBMI = getString(R.string.bmi_level_stage2);
            type = 7;
        }
        else {
            viewId = R.id.viewColorStage3;
            nameLevelBMI = getString(R.string.bmi_stage_3);
            levelBMI = getString(R.string.bmi_level_stage3);
            type = 8;
        }

        layoutParams.startToStart = viewId;
        layoutParams.endToEnd = viewId;
        binding.tvNameLevel.setText(nameLevelBMI);
        binding.tvLevel.setText(levelBMI);
        binding.imgArrow.setLayoutParams(layoutParams);
    }

    private void displayOverlaySuccess()
    {
        dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.overlay_success);
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
