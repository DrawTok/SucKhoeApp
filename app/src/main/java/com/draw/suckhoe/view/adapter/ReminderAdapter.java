package com.draw.suckhoe.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.draw.suckhoe.databinding.ItemReminderBinding;
import com.draw.suckhoe.model.Reminder;
import com.draw.suckhoe.myInterface.OnClickItemListener;
import com.draw.suckhoe.view.viewModels.ReminderViewModel;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    ItemReminderBinding binding;
    List<Reminder> list;
    OnClickItemListener onClickItemListener;
    ReminderViewModel reminderViewModel;


    public void setList(ReminderViewModel reminderViewModel, List<Reminder> list)
    {
        this.reminderViewModel = reminderViewModel;
        this.list = list;
    }
    public void setOnClickItemListener(OnClickItemListener onClickItemListener)
    {
        this.onClickItemListener = onClickItemListener;
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
        holder.itemView.setOnClickListener(v->
        {if(onClickItemListener != null)
            onClickItemListener.onClickItemListener(position);});
        holder.binding.switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                reminder.setIsSwitchOn(1);
            else
                reminder.setIsSwitchOn(0);
            reminderViewModel.updateReminder(reminder);
        });
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
