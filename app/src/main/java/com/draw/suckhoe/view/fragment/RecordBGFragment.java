package com.draw.suckhoe.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.draw.suckhoe.R;
import com.draw.suckhoe.database.HealthDB;
import com.draw.suckhoe.databinding.RecordBgFragmentBinding;
import com.draw.suckhoe.factories.ViewModelFactory;
import com.draw.suckhoe.model.BloodGlucose;
import com.draw.suckhoe.utils.LevelResult;
import com.draw.suckhoe.view.activity.DetailsActivity;
import com.draw.suckhoe.view.viewModels.BloodGlucoseViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordBGFragment extends Fragment {

    RecordBgFragmentBinding binding;
    Vibrator vibrator;

    private float preValue = 0.0f; // Biến để xử lý rung khi thay đổi value trên rulerView
    HealthDB healthDB;
    private BloodGlucose bloodGlucose;
    private LevelResult levelResult;
    private BloodGlucoseViewModel viewModel;

    private boolean isSuccess = false;

    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RecordBgFragmentBinding.inflate(inflater, container, false);

        DetailsActivity activity= (DetailsActivity) getActivity();

        healthDB = HealthDB.getInstance(requireContext());

        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(BloodGlucoseViewModel.class);

        vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

        binding.ruler.initViewParam(0, 0, 35f, 0.1f);

        viewModel.getValueLv().observe(getViewLifecycleOwner(), value -> {});

        binding.ruler.setChooseValueChangeListener(v -> {
            if(Math.abs(v-preValue) > 0) {
                handleVibrator();
                preValue = v;
            }
            viewModel.getLevelResultLiveData().observe(getViewLifecycleOwner(), result -> {
                if(result != null)
                    levelResult = result;
            });
            viewModel.setGlucoseLevel(v);
            changeNameBGlucose();
        });

        binding.tvConfirm.setOnClickListener(v -> {
            bloodGlucose = new BloodGlucose(0, preValue, getTimeNow(), levelResult.getType());
            viewModel.insertBGlucose(bloodGlucose);
            new Handler().postDelayed(() -> {
                if(isSuccess){
                    displayOverlaySuccess();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("bGlucose_info", bloodGlucose);
                    bundle.putInt("IS_NEW_DATA", 1);
                    BGSuccessFragment bgSuccessFragment = new BGSuccessFragment();
                    bgSuccessFragment.setArguments(bundle);
                    new Handler().postDelayed(() -> {
                        dialog.dismiss();
                        if(activity != null)
                            activity.replaceFragment(bgSuccessFragment);
                    }, 470);
                }else
                {
                    Toast.makeText(activity, R.string.new_add_error, Toast.LENGTH_SHORT).show();
                }
            }, 100);
        });

        viewModel.getInsertResultLiveData().observe(getViewLifecycleOwner(), result -> isSuccess = result);

        return binding.getRoot();
    }

    private void handleVibrator() {
        if(vibrator.hasVibrator())
        {
            VibrationEffect effect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE);
            vibrator.vibrate(effect);
        }
    }

    private void changeNameBGlucose()
    {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.imgArrow.getLayoutParams();

        int viewId;

        switch (levelResult.getType()) {
            case 1:
                viewId = R.id.viewColorLow;
                break;
            case 2:
                viewId = R.id.viewColorNormal;
                break;
            case 3:
                viewId = R.id.viewColorHigh;
                break;
            default:
                viewId = R.id.viewColorStage1;
                break;
        }

        layoutParams.startToStart = viewId;
        layoutParams.endToEnd = viewId;

        binding.tvNameLevel.setText(levelResult.getNameRes());
        binding.tvLevel.setText(levelResult.getLevelRes());
        binding.imgArrow.setLayoutParams(layoutParams);
    }
    private String getTimeNow()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
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
