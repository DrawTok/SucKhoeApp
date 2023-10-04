package com.draw.suckhoe.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.draw.suckhoe.R;
import com.draw.suckhoe.databinding.BottomLayoutBinding;
import com.draw.suckhoe.databinding.DeleteAlarmDialogBinding;
import com.draw.suckhoe.databinding.ReminderFragmentBinding;
import com.draw.suckhoe.factories.ReminderViewModelFactory;
import com.draw.suckhoe.model.Reminder;
import com.draw.suckhoe.myInterface.OnClickItemListener;
import com.draw.suckhoe.view.adapter.ReminderAdapter;
import com.draw.suckhoe.view.viewModels.ReminderViewModel;
import com.shawnlin.numberpicker.NumberPicker;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReminderFragment extends Fragment implements OnClickItemListener {

    private ReminderFragmentBinding binding;
    private Vibrator vibrator;
    private List<Reminder> list;

    private ReminderViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ReminderFragmentBinding.inflate(inflater, container, false);
        vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

        // Khởi tạo ViewModel
        ReminderViewModelFactory factory = new ReminderViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(ReminderViewModel.class);

        observeReminderData();

        //thực hiện thêm sự kiện cuộn numberPicker và hiển thị dialog
        binding.tvAddNewReminder.setOnClickListener(v -> {
            BottomLayoutBinding bottomBinding = BottomLayoutBinding.inflate(getLayoutInflater());
            setScrollListenerNumberPicker(bottomBinding.wheelHour);
            setScrollListenerNumberPicker(bottomBinding.wheelMinutes);
            showDialogBottom(bottomBinding, null, "Thêm mới");
        });

        return binding.getRoot();
    }

    @Override
    public void onClickItemListener(int pos) {
        BottomLayoutBinding bottomBinding = BottomLayoutBinding.inflate(getLayoutInflater());
        setScrollListenerNumberPicker(bottomBinding.wheelHour);
        setScrollListenerNumberPicker(bottomBinding.wheelMinutes);
        Reminder reminder = list.get(pos);
        showDialogBottom(bottomBinding, reminder, "Cập nhật");
    }

    //hiển thị dialog để thực hiện các hành động thêm mới, cập nhật, xóa nhắc nhở
    private void showDialogBottom(BottomLayoutBinding bottomBinding, Reminder reminder, String textBtn) {
        final Dialog dialog = new Dialog(requireContext());
        if(reminder != null)
            bottomBinding.tvBotLayoutName.setText(reminder.getNameReminder());
        else
            bottomBinding.tvDeleteReminder.setVisibility(View.GONE);
        bottomBinding.tvConfirmReminder.setText(textBtn);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(bottomBinding.getRoot());
        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }

        //thêm mới nhắc nhở
        bottomBinding.tvConfirmReminder.setOnClickListener(v -> {
            addReminder(bottomBinding);
            dialog.dismiss();
        });

        //xóa item nhắc nhở
        bottomBinding.tvDeleteReminder.setOnClickListener(v -> {
            if(reminder != null)
            {
                showDeleteDialog(reminder);
                dialog.dismiss();
            }
        });
    }

    //thiết lập rung trong view NumberPicker
    private void setScrollListenerNumberPicker(NumberPicker numberPicker) {
        numberPicker.setOnScrollListener((view, scrollState) -> handleVibrate());
    }

    //xử lý rung khi thay đổi giá trị trong NumberPicker
    private void handleVibrate() {
        if (vibrator.hasVibrator()) {
            VibrationEffect vibrationEffect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE);
            vibrator.vibrate(vibrationEffect);
        }
    }

    //Hàm xử lý thêm dữ liệu vào database
    private void addReminder(BottomLayoutBinding bottomBinding) {
        String nameReminder = (String) bottomBinding.tvBotLayoutName.getText();
        int hour = bottomBinding.wheelHour.getValue();
        int minutes = bottomBinding.wheelMinutes.getValue();
        String dayReminder = "";

        if (bottomBinding.checkMon.isChecked())
            dayReminder += "Mon, ";
        if (bottomBinding.checkTus.isChecked())
            dayReminder += "Tus, ";
        if (bottomBinding.checkWed.isChecked())
            dayReminder += "Wed, ";
        if (bottomBinding.checkThu.isChecked())
            dayReminder += "Thu, ";
        if (bottomBinding.checkFri.isChecked())
            dayReminder += "Fri, ";
        if (bottomBinding.checkSat.isChecked())
            dayReminder += "Sat, ";
        if (bottomBinding.checkSun.isChecked())
            dayReminder += "Sun, ";

        viewModel.insertReminder(new Reminder(0, nameReminder, dateFormat(hour, minutes), dayReminder, 1));
    }


    //live data lấy ra danh sách nhắc nhở trong database
    private void observeReminderData() {
        viewModel.getListDataReminder();
        list = new ArrayList<>();
        viewModel.getReminderListLiveData().observe(getViewLifecycleOwner(), reminders -> {
            if (reminders != null) {
                list = reminders;
                setupAdapter();
            }
        });
    }

    //setup adapter nhắc nhở
    private void setupAdapter() {
        binding.recyclerReminder.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerReminder.setHasFixedSize(true);
        ReminderAdapter adapter = new ReminderAdapter();
        adapter.setList(list);
        adapter.setOnClickItemListener(this);
        binding.recyclerReminder.setAdapter(adapter);
    }

    //format thời gian
    private String dateFormat(int hour, int minutes)
    {
        LocalTime localTime = LocalTime.of(hour, minutes);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localTime.format(formatter);
    }

    //hiển thị hộp thoại xác nhận có xóa item hay không
    private void showDeleteDialog(Reminder reminder)
    {
        DeleteAlarmDialogBinding alarmDialogBinding =
                DeleteAlarmDialogBinding.inflate(LayoutInflater.from(getContext()));
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(alarmDialogBinding.getRoot());
        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.CENTER);
        }

        alarmDialogBinding.tvCancel.setOnClickListener(v -> dialog.dismiss());
        alarmDialogBinding.tvAccept.setOnClickListener(v -> {
            viewModel.deleteReminder(reminder.getIdReminder());
            dialog.dismiss();
        });
    }
}
