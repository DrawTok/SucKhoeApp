package com.draw.suckhoe.view.activity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.draw.suckhoe.R;
import com.draw.suckhoe.databinding.ActivityMainBinding;
import com.draw.suckhoe.view.fragment.DashBoardFragment;
import com.draw.suckhoe.view.fragment.DetailFragment;
import com.draw.suckhoe.view.fragment.HealthMonitorFragment;
import com.draw.suckhoe.view.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setStatusBarColor();
        binding.bottomNav.setItemIconTintList(null);
        replaceFragment(new DashBoardFragment());
        binding.bottomNav.setOnItemSelectedListener(item ->
        {
            int itemId = item.getItemId();
            if(itemId == R.id.homePage)
                replaceFragment(new DashBoardFragment());
            else if(itemId == R.id.monitor)
                replaceFragment(new HealthMonitorFragment());
            else if(itemId == R.id.details)
                replaceFragment(new DetailFragment());
            else if(itemId == R.id.setting)
                replaceFragment(new SettingFragment());
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackCount = fragmentManager.getBackStackEntryCount();
        if(backStackCount == 1)
        {
            showDialogCloseApp();
        } else if (backStackCount > 1) {
            for (int i = 1; i < backStackCount; i++) {
                fragmentManager.popBackStack();
            }
            binding.bottomNav.getMenu().findItem(R.id.homePage).setChecked(true);
        } else {
            super.onBackPressed();
        }
    }
    private void showDialogCloseApp() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_exit);
        //Loại bỏ nền mờ của dialog
        if(dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        AppCompatButton btnContinueApp = dialog.findViewById(R.id.btnContinueApp);
        AppCompatButton btnCloseApp = dialog.findViewById(R.id.btnCloseApp);
        btnContinueApp.setOnClickListener(v-> finish());
        btnCloseApp.setOnClickListener(v-> dialog.dismiss());
        dialog.show();
    }

    private void setStatusBarColor() {
        Window window = this.getWindow();
        //bật cờ cho cửa sổ hiện tại để cho phép vẽ nền của thanh trạng thái
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //xóa cờ nếu nó đã được đặt. Cờ này liên quan đến việc làm cho thanh trạng thái trong suốt
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        //thay đổi màu icon hoặc text trên statusBar
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
}