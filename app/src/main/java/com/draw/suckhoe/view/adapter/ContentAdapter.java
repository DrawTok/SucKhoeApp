package com.draw.suckhoe.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.draw.suckhoe.databinding.ItemContentBinding;
import com.draw.suckhoe.model.Items;

import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> {

    List<Items> list;
    public void setList(List<Items> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContentBinding binding = ItemContentBinding.inflate
                (LayoutInflater.from(parent.getContext()), parent, false);
        return new ContentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        Items item = list.get(position);
        holder.binding.titleContent.setText(item.getItemName());
        holder.binding.tvContent.setText(item.getContent());
    }

    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();
        return 0;
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ItemContentBinding binding;
        public ContentViewHolder(ItemContentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
