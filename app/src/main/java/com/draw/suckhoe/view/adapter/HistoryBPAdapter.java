package com.draw.suckhoe.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.draw.suckhoe.databinding.ItemHistoryBinding;
import com.draw.suckhoe.model.BloodPressure;

import java.text.MessageFormat;
import java.util.List;

public class HistoryBPAdapter extends RecyclerView.Adapter<HistoryBPAdapter.HistoryBPViewHolder> {

    ItemHistoryBinding binding;
    private List<BloodPressure> list;
    private int size;

    public void setList(List<BloodPressure> list, int size)
    {
        this.list = list;
        this.size = size;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public HistoryBPViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HistoryBPViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryBPViewHolder holder, int position) {
        BloodPressure bloodPressure = list.get(position);
        holder.binding.tvSysAndDias.setText(MessageFormat.format("{0}\n{1}", bloodPressure.getSystolic(), bloodPressure.getDiastolic()));
        holder.binding.tvPulse.setText(MessageFormat.format("Pulse: {0}PBM", bloodPressure.getPulse()));
        holder.binding.tvTime.setText(bloodPressure.getTime());
    }

    @Override
    public int getItemCount() {
        if(list != null)
        {
            if(size <= 4)
                return size;
            else
                return list.size();

        }
        return 0;
    }

    public static class HistoryBPViewHolder extends RecyclerView.ViewHolder {
        ItemHistoryBinding binding;
    public HistoryBPViewHolder(@NonNull ItemHistoryBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}

}
