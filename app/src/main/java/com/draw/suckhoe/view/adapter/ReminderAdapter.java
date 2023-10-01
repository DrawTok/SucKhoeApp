package com.draw.suckhoe.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.draw.suckhoe.databinding.ItemReminderBinding;
import com.draw.suckhoe.model.Reminder;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    ItemReminderBinding binding;
    List<Reminder> list;

    public void setList(List<Reminder> list)
    {
        this.list = list;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemReminderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ReminderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = list.get(position);
        holder.binding.tvNameReminder.setText(reminder.getNameReminder());
        holder.binding.tvTimeReminder.setText(reminder.getTimeReminder());
        holder.binding.tvDayReminder.setText(reminder.getDayReminder());
        holder.binding.switchReminder.setChecked(reminder.getIsSwitchOn() == 1);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {
        ItemReminderBinding binding;
        public ReminderViewHolder(@NonNull ItemReminderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
