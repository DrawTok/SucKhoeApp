package com.draw.suckhoe.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.draw.suckhoe.R;
import com.draw.suckhoe.databinding.ActivityDetailsBinding;
import com.draw.suckhoe.utils.MyConstants;
import com.draw.suckhoe.view.fragment.BGDetailFragment;
import com.draw.suckhoe.view.fragment.BMIDetailFragment;
import com.draw.suckhoe.view.fragment.BPDetailFragment;
import com.draw.suckhoe.view.fragment.RecordBGFragment;
import com.draw.suckhoe.view.fragment.RecordBMIFragment;
import com.draw.suckhoe.view.fragment.RecordBPFragment;
import com.draw.suckhoe.view.fragment.ReminderFragment;
import com.draw.suckhoe.view.viewModels.DetailsViewModel;

public class DetailsActivity extends AppCompatActivity {

    ActivityDetailsBinding binding;
    DetailsViewModel viewModel;
    private String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        int fragmentId = intent.getIntExtra("idFragment", -1);
        setStatusBarColor();
        viewModel = new DetailsViewModel();
        binding.setDetailsViewModel(viewModel);
        if(fragmentId == MyConstants.BLOOD_PRESSURE_ID)
        {
            title = "Huyết áp";
            replaceFragment(new BPDetailFragment());
        }else if(fragmentId == MyConstants.BLOOD_GLUCOSE_ID)
        {
            title = "Đường huyết";
            replaceFragment(new BGDetailFragment());
        }else if(fragmentId == MyConstants.BLOOD_BMI_ID)
        {
            replaceFragment(new BMIDetailFragment());
        }

        setTitleName(title);

        viewModel.getNavigateBack().observe(this, isBack -> {
            if(isBack) {
                handlePressBack();
            }
        });

       viewModel.getIsVisibility().observe(this, isVisibility ->
               binding.tvAddNewRecord.setVisibility(isVisibility ? View.VISIBLE : View.GONE));


       viewModel.getLiveDataTitle().observe(this, s -> binding.tvActivityTitle.setText(s));

        binding.tvAddNewRecord.setOnClickListener(v->
        {
            viewModel.setIsVisibility(false);
            if(fragmentId == MyConstants.BLOOD_PRESSURE_ID)
            {
                replaceFragment(new RecordBPFragment());
            }else if(fragmentId == MyConstants.BLOOD_GLUCOSE_ID)
            {
                replaceFragment(new RecordBGFragment());
            }else if(fragmentId == MyConstants.BLOOD_BMI_ID)
            {
                replaceFragment(new RecordBMIFragment());
            }

        });

        binding.timer.setOnClickListener(v ->
        {
            binding.tvAddNewRecord.setVisibility(View.GONE);
            replaceFragment(new ReminderFragment());
        });

        binding.tvFinish.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            while (fragmentManager.getBackStackEntryCount() > 1) {
                fragmentManager.popBackStackImmediate();
            }
            binding.tvFinish.setVisibility(View.GONE);
            binding.timer.setVisibility(View.VISIBLE);
            binding.tvAddNewRecord.setVisibility(View.VISIBLE);
        });

        backPressed();
    }

    private void backPressed() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handlePressBack();
            }
        });
    }

    private void setStatusBarColor() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_detail, fragment).addToBackStack(null).commit();
    }

    private void handlePressBack()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStack = fragmentManager.getBackStackEntryCount();
        if(binding.timer.getVisibility() != View.VISIBLE)
            binding.timer.setVisibility(View.VISIBLE);
        if(binding.tvFinish.getVisibility() != View.GONE)
            binding.tvFinish.setVisibility(View.GONE);
        if(binding.tvDelete.getVisibility() != View.GONE)
            binding.tvDelete.setVisibility(View.GONE);
        if(backStack == 2)
        {
            fragmentManager.popBackStack();
            viewModel.setIsVisibility(true);
            setTitleName(title);
        }else if(backStack > 2) {
            fragmentManager.popBackStack();
        }
        else
            finish();
    }

    public void setTitleName(String name)
    {
        viewModel.setTitle(name);
    }
}