package com.draw.suckhoe.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.draw.suckhoe.databinding.ItemBgDataBinding;
import com.draw.suckhoe.model.BloodGlucose;

import java.util.List;

public class BGDataAdapter extends RecyclerView.Adapter<BGDataAdapter.BGDataViewHolder> {
    ItemBgDataBinding binding;
    List<BloodGlucose> list;
    @NonNull
    @Override
    public BGDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemBgDataBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BGDataViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BGDataViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public static class BGDataViewHolder extends RecyclerView.ViewHolder {
        ItemBgDataBinding binding;
        public BGDataViewHolder(@NonNull ItemBgDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
