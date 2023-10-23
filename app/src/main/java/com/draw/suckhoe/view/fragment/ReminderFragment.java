package com.draw.suckhoe.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.draw.suckhoe.R;
import com.draw.suckhoe.databinding.BottomLayoutBinding;
import com.draw.suckhoe.databinding.DeleteAlarmDialogBinding;
import com.draw.suckhoe.databinding.ReminderFragmentBinding;
import com.draw.suckhoe.factories.ViewModelFactory;
import com.draw.suckhoe.helper.AlarmReceiver;
import com.draw.suckhoe.model.Reminder;
import com.draw.suckhoe.myInterface.OnClickItemListener;
import com.draw.suckhoe.view.adapter.ReminderAdapter;
import com.draw.suckhoe.view.viewModels.ReminderViewModel;
import com.shawnlin.numberpicker.NumberPicker;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
        checkPermissions();
        // Khởi tạo ViewModel
        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
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

        Pair<Integer, Integer> timeInt;
        if(textBtn.equals("Cập nhật") && reminder != null)
        {
            timeInt = convertTimeToInt(reminder.getTimeReminder());
        }else
        {
            String time = getTimeNow();
            timeInt = convertTimeToInt(time);
        }

        bottomBinding.wheelHour.setValue(timeInt.first);
        bottomBinding.wheelMinutes.setValue(timeInt.second);


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

    private String getTimeNow() {
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return format.format(currentTime.getTime());

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
        AppCompatCheckBox[] dayValues = {bottomBinding.checkMon, bottomBinding.checkTus, bottomBinding.checkWed,
                bottomBinding.checkThu, bottomBinding.checkFri, bottomBinding.checkSat, bottomBinding.checkSun};
        String[] daysOfWeek = {"Mon", "Tus", "Wed", "Thu", "Fri", "Sat", "Sun"};
        StringBuilder dayReminderBuilder = new StringBuilder();

        for (int i = 0; i < dayValues.length; i++) {
            if (dayValues[i].isChecked()) {
                dayReminderBuilder.append(daysOfWeek[i]).append(", ");
            }
        }

        String dayReminder = dayReminderBuilder.toString().trim();

        int[] dayFlags = new int[7];
        for (int i = 0; i < dayValues.length; i++) {
            dayFlags[i] = dayValues[i].isChecked() ? 1 : 0;
        }
        Reminder reminder = new Reminder(0, nameReminder, dateFormat(hour, minutes),
                dayReminder, dayFlags[0], dayFlags[1], dayFlags[2], dayFlags[3], dayFlags[4], dayFlags[5], dayFlags[6], 1);
        viewModel.insertReminder(reminder);
        generateAlarmClock(reminder);
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
        adapter.setList(viewModel,list);
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
            AlarmReceiver alarmReceiver = new AlarmReceiver();
            alarmReceiver.removeScheduler(reminder);
            dialog.dismiss();
        });
    }

    private void generateAlarmClock(Reminder reminder) {
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.addReminder(reminder);
        alarmReceiver.scheduleReminders(requireContext());

    }
    private void checkPermissions()
    {
        // Kiểm tra và yêu cầu quyền thông báo
    }

    public Pair<Integer, Integer> convertTimeToInt(String time)
    {
        String[] times = time.split(":");
        int hour = Integer.parseInt(times[0]);
        int minute = Integer.parseInt(times[1]);
        return new Pair<>(hour, minute);
    }
}
