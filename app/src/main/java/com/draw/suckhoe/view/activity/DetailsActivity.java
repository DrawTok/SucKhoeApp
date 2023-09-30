package com.draw.suckhoe.view.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.draw.suckhoe.R;
import com.draw.suckhoe.databinding.ActivityDetailsBinding;
import com.draw.suckhoe.view.fragment.BPDetailFragment;
import com.draw.suckhoe.view.fragment.RecordBPFragment;
import com.draw.suckhoe.view.viewModels.DetailsViewModel;

public class DetailsActivity extends AppCompatActivity {

    ActivityDetailsBinding binding;
    DetailsViewModel viewModel;
    private final String title = "Huyết áp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setStatusBarColor();

        replaceFragment(new BPDetailFragment());
        viewModel = new DetailsViewModel();
        binding.setDetailsViewModel(viewModel);
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
            replaceFragment(new RecordBPFragment());
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

    @Override
    public void onBackPressed() {
        handlePressBack();
    }

    private void handlePressBack()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStack = fragmentManager.getBackStackEntryCount();
        if(backStack == 2)
        {
            fragmentManager.popBackStack();
            viewModel.setIsVisibility(true);
            setTitleName(title);
        }else if(backStack > 2) {
            if(binding.timer.getVisibility() != View.VISIBLE)
                binding.timer.setVisibility(View.VISIBLE);
            if(binding.history.getVisibility() != View.VISIBLE)
                binding.history.setVisibility(View.VISIBLE);
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