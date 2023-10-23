package com.draw.suckhoe.view.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.draw.suckhoe.R;
import com.draw.suckhoe.databinding.ItemHistoryBinding;
import com.draw.suckhoe.model.BloodGlucose;
import com.draw.suckhoe.model.BloodPressure;
import com.draw.suckhoe.myInterface.OnClickItemListener;
import com.draw.suckhoe.utils.LevelResult;
import com.draw.suckhoe.utils.ViewColorRenderer;

import java.text.MessageFormat;
import java.util.List;

public class GenericAdapter<T> extends RecyclerView.Adapter<GenericAdapter.GenericViewHolder> {

    private List<T> list;
    OnClickItemListener listener;

    public void setList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setListener(OnClickItemListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoryBinding binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        T item = list.get(position);

        holder.itemView.setOnClickListener(v -> listener.onClickItemListener(position));
        Context context = holder.itemView.getContext();
        if (item instanceof BloodPressure) {
            BloodPressure bloodPressure = (BloodPressure) item;
            // Sử dụng các thuộc tính cụ thể của BloodPressure
            holder.binding.tvValueLevel.setText(MessageFormat.format("{0}\n{1}", bloodPressure.getSystolic(), bloodPressure.getDiastolic()));
            holder.binding.tvMoreInfo.setText(MessageFormat.format("Pulse: {0} PBM", bloodPressure.getPulse()));
            holder.binding.tvTime.setText(bloodPressure.getTime());
            LevelResult levelResult = new ViewColorRenderer().renderColorView(bloodPressure, context);
            holder.binding.tvNameLevel.setText(levelResult.getNameRes());
            holder.binding.viewColor.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(context, levelResult.getType())));
        }else if(item instanceof BloodGlucose)
        {
            BloodGlucose bloodGlucose = (BloodGlucose) item;
            holder.binding.tvValueLevel.setText(String.valueOf(bloodGlucose.getLevelBGlucose()));
            holder.binding.tvUnit.setText(context.getString(R.string.unit_bg));
            holder.binding.tvMoreInfo.setVisibility(View.GONE);
            holder.binding.tvTime.setText(bloodGlucose.getTime());
            LevelResult levelResult = new ViewColorRenderer().renderColorView(bloodGlucose, context);
            holder.binding.tvNameLevel.setText(levelResult.getNameRes());
            holder.binding.viewColor.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(context, levelResult.getType())));
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public static class GenericViewHolder extends RecyclerView.ViewHolder {
        ItemHistoryBinding binding;

        public GenericViewHolder(@NonNull ItemHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
