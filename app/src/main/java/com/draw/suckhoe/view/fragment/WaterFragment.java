package com.draw.suckhoe.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.chingtech.rulerview.library.RulerView;
import com.draw.suckhoe.R;
import com.draw.suckhoe.databinding.WaterFragmentBinding;
import com.draw.suckhoe.factories.ViewModelFactory;
import com.draw.suckhoe.model.Water;
import com.draw.suckhoe.utils.MyConstants;
import com.draw.suckhoe.view.activity.DetailsActivity;
import com.draw.suckhoe.view.viewModels.WaterViewModel;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WaterFragment extends Fragment {

    private static final String PREF_MAX_WATER = "MAX_WATER";
    private static final String PREF_ML = "ml";
    private WaterFragmentBinding binding;
    private Vibrator vibrator;
    private int preValue = 0;
    private int numOfCups = 0;
    private int goalDrink = 0;
    private Water water;
    private WaterViewModel waterViewModel;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WaterFragmentBinding.inflate(inflater, container, false);

        vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

        sharedPref = requireContext().getSharedPreferences(MyConstants.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        setUpViewModel();
        setUpUI();
        setUpAction();
        return binding.getRoot();
    }

    private void setUpUI() {

        int currentProgress = sharedPref.getInt(MyConstants.PREF_CURRENT_PROGRESS, 0);
        numOfCups = sharedPref.getInt(MyConstants.PREF_NUM_OF_CUPS, 0);
        preValue = sharedPref.getInt(MyConstants.PREF_DEF_WATER, 200);
        goalDrink = sharedPref.getInt(MyConstants.GOAL_DRINK_WATER, 2000);

        binding.waveWater.setProgress(currentProgress);
        binding.waveWater.setMaxProgress(goalDrink);
        binding.waveWater.setDefProgress(preValue);

        water = waterViewModel.getLatestWater();
        binding.valueRecentWater.setText(String.format("%sml", water != null ? water.getMlWater() : "--"));

        binding.valueNumOfCups.setText(String.format("%s Cốc", numOfCups));
        binding.tvGoals.setText(String.format("%sml", goalDrink));

        if (getActivity() != null) {
            getActivity().findViewById(R.id.tvAddNewRecord).setVisibility(View.GONE);
        }
    }

    private void setUpViewModel() {
        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
        waterViewModel = new ViewModelProvider(this, factory).get(WaterViewModel.class);
    }

    private void setUpAction() {
        binding.constraintDailyGoals.setOnClickListener(v -> showDialogSetMaxLevelWater());
        binding.btnIncrease.setOnClickListener(v -> onIncreaseButtonClick());
        binding.btnDecrease.setOnClickListener(v -> onDecreaseButtonClick());
        binding.btnSetUp.setOnClickListener(v -> changeAmountOfWater());

        binding.tvHistory.setOnClickListener(v->
        {
            DetailsActivity activity = (DetailsActivity) getActivity();
            if(activity != null)
                activity.replaceFragment(new WaterDetailFragment());
        });
    }

    private void onIncreaseButtonClick() {
        binding.waveWater.setDefProgress(sharedPref.getInt(MyConstants.PREF_DEF_WATER, 200));
        binding.waveWater.increaseProgress();
        int isGoals = 0;
        if (binding.waveWater.getProgress() >= binding.waveWater.getMaxProgress()) {
            isGoals = 1;
        }
        water = new Water(0, binding.waveWater.getDefProgress(), getTimeNow(), isGoals);
        waterViewModel.insertDataWater(water);
        binding.valueRecentWater.setText(String.format("%sml", water.getMlWater()));
        numOfCups += 1;
        binding.valueNumOfCups.setText(String.format("%s Cốc", numOfCups));
    }

    private void onDecreaseButtonClick() {
        water = waterViewModel.getLatestWater();
        if (water != null) {
            binding.waveWater.setDefProgress(water.getMlWater());
            waterViewModel.deleteDataWater(water);
            binding.waveWater.decreaseProgress();
            numOfCups -= 1;

            if (numOfCups <= 0) {
                numOfCups = 0;
                binding.valueRecentWater.setText(String.format("--%s", requireContext().getString(R.string.ml)));
            }else
            {
                binding.valueRecentWater.setText(String.format("%sml", waterViewModel.getLatestWater().getMlWater()));
            }
            binding.valueNumOfCups.setText(String.format("%s Cốc", numOfCups));
        }
    }

    private void showDialogSetMaxLevelWater() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_setllevel_water);
        setupDialog(dialog, R.style.DialogAnimation, PREF_MAX_WATER);
        AppCompatButton btnSkip = dialog.findViewById(R.id.btnSkip);
        AppCompatButton btnAcp = dialog.findViewById(R.id.btnAcp);
        EditText edtLevelWater = dialog.findViewById(R.id.edtLevelWater);
        edtLevelWater.setText(String.valueOf(binding.waveWater.getMaxProgress()));
        btnSkip.setOnClickListener(v -> dialog.dismiss());
        btnAcp.setOnClickListener(v -> {
            onAcceptMaxLevelWaterClick(edtLevelWater);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void onAcceptMaxLevelWaterClick(EditText edtLevelWater) {
        goalDrink = Integer.parseInt(edtLevelWater.getText().toString());
        binding.tvGoals.setText(String.format("%sml", goalDrink));
        binding.waveWater.setMaxProgress(goalDrink);
    }

    private void changeAmountOfWater() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_hw_bmi);
        setupDialog(dialog, R.style.DialogAnimation, PREF_ML);
        TextView tvNameDialog = dialog.findViewById(R.id.tvNameDialog);
        TextView tvUnit = dialog.findViewById(R.id.tvUnitOfRuler);
        RulerView rulerView = dialog.findViewById(R.id.ruler);
        MaterialButton button = dialog.findViewById(R.id.btnConfirm);
        tvNameDialog.setText(requireContext().getString(R.string.luong_nuoc_uong));
        tvUnit.setText(requireContext().getString(R.string.ml));
        preValue = sharedPref.getInt(MyConstants.PREF_DEF_WATER, 200);
        rulerView.initViewParam(preValue, 10, 500, 1);
        setUpRulerViewListener(rulerView);
        button.setOnClickListener(v -> {
            onConfirmAmountOfWaterClick();
            dialog.dismiss();
        });
        dialog.show();
    }

    private void setUpRulerViewListener(RulerView rulerView) {
        rulerView.setChooseValueChangeListener(v -> {
            if (preValue != v) {
                handleVibrator();
                preValue = (int) v;
            }
        });
    }

    private void onConfirmAmountOfWaterClick() {
        binding.waveWater.setDefProgress(preValue);
        editor.putInt(MyConstants.PREF_DEF_WATER, preValue);
        editor.commit();
    }

    private void handleVibrator() {
        if (vibrator.hasVibrator()) {
            VibrationEffect effect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE);
            vibrator.vibrate(effect);
        }
    }

    private void setupDialog(Dialog dialog, int animationStyle, String typeDialog) {
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = animationStyle;
        }
        if (typeDialog.equals(PREF_ML)) {
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        } else {
            dialog.getWindow().setGravity(Gravity.CENTER);
        }
    }

    private String getTimeNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void saveData() {
        editor.putInt(MyConstants.PREF_CURRENT_PROGRESS, binding.waveWater.getProgress());
        editor.putInt(MyConstants.PREF_NUM_OF_CUPS, numOfCups);
        editor.putInt(MyConstants.GOAL_DRINK_WATER, goalDrink);
        editor.apply();
    }

    @Override
    public void onStop() {
        super.onStop();
        saveData();
    }
}
