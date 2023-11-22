package com.draw.suckhoe.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.draw.suckhoe.databinding.ItemWaterBinding;
import com.draw.suckhoe.model.Water;

import java.util.List;

public class WaterAdapter extends RecyclerView.Adapter<WaterAdapter.WaterViewHolder> {

    ItemWaterBinding binding;
    List<Water> list;

    public void setList(List<Water> list)
    {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemWaterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WaterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WaterViewHolder holder, int position) {
        Water water = list.get(position);
        binding.tvLvWater.setText(String.valueOf(water.getMlWater()));
        binding.tvTime.setText(water.getTime());
    }

    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();
        return 0;
    }

    public static class WaterViewHolder extends RecyclerView.ViewHolder {
        ItemWaterBinding binding;
        public WaterViewHolder(@NonNull ItemWaterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
