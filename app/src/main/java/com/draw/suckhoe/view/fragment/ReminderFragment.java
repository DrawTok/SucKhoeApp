package com.draw.suckhoe.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.draw.suckhoe.databinding.ReminderFragmentBinding;
import com.draw.suckhoe.model.Reminder;
import com.draw.suckhoe.view.adapter.ReminderAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReminderFragment extends Fragment {

    ReminderFragmentBinding binding;
    ReminderAdapter adapter;

    List<Reminder> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ReminderFragmentBinding.inflate(inflater, container, false);
        generateExampleData();
        binding.recyclerReminder.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerReminder.setHasFixedSize(true);
        adapter = new ReminderAdapter();
        adapter.setList(list);
        binding.recyclerReminder.setAdapter(adapter);
        return binding.getRoot();
    }

    private void generateExampleData()
    {
        list = new ArrayList<>();
        list.add(new Reminder("Ghi lại Huyết áp", "22:28", "Sun, Mon, Tue, Wed, Thu", 1));
        list.add(new Reminder("Ghi lại Đường huyết", "22:28", "Sun, Mon, Tue, Wed", 0));
        list.add(new Reminder("Ghi lại Cân nặng", "22:30", "Sun, Mon, Tue, Wed, Thu, Fri", 1));
        list.add(new Reminder("Ghi lại Huyết áp", "22:56", "Sun, Mon, Tue, Wed", 1));
        list.add(new Reminder("Ghi lại Đường huyết", "22:10", "Sun, Mon, Tue, Thu", 0));
        list.add(new Reminder("Ghi lại Đường huyết", "22:12", "Sun, Mon, Wed, Thu", 1));
    }
}
