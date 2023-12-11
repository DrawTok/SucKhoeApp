package com.draw.suckhoe.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.draw.suckhoe.R;
import com.draw.suckhoe.databinding.ItemDetailInfoBinding;
import com.draw.suckhoe.model.Titles;
import com.draw.suckhoe.myInterface.OnClickItemListener;

import java.util.List;

public class InfoDetailsAdapter extends RecyclerView.Adapter<InfoDetailsAdapter.InfoDetailViewHolder> {

    List<Titles> list;
    Context context;
    OnClickItemListener listener;

    public void setList(List<Titles> list, Context context)
    {
        this.list = list;
        this.context = context;
        notifyDataSetChanged();
    }

    public void setListener(OnClickItemListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public InfoDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDetailInfoBinding binding = ItemDetailInfoBinding.
                inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new InfoDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoDetailViewHolder holder, int position) {
        Titles titles = list.get(position);
        holder.itemView.setOnClickListener(v->
        {
            listener.onClickItemListener(position);
        });
        holder.binding.tvDetailTitle.setText(titles.getTitleName());
        String imageUrl = titles.getImageUrl().trim();
        Glide.with(context).load(imageUrl).override(200, 200).into(holder.binding.imvDetailTitle);
    }

    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();
        return 0;
    }

    public static class InfoDetailViewHolder extends RecyclerView.ViewHolder {
        ItemDetailInfoBinding binding;
        public InfoDetailViewHolder(ItemDetailInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
