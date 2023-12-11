package com.draw.suckhoe.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.draw.suckhoe.databinding.DetailFragmentBinding;
import com.draw.suckhoe.utils.MyConstants;
import com.draw.suckhoe.view.activity.DetailsActivity;


public class DetailFragment extends Fragment
{
    DetailFragmentBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DetailFragmentBinding.inflate(inflater, container, false);

        binding.constrainDetailBP.setOnClickListener(v -> startDetailsActivity(MyConstants.DETAIL_INFO_BP));
        binding.constrainDetailBG.setOnClickListener(v -> startDetailsActivity(MyConstants.DETAIL_INFO_BG));
        binding.constrainDetailWeight.setOnClickListener(v -> startDetailsActivity(MyConstants.DETAIL_INFO_WEIGHT));
        binding.constrainDetailDiet.setOnClickListener(v -> startDetailsActivity(MyConstants.DETAIL_INFO_DIET));

        return binding.getRoot();
    }

    private void startDetailsActivity(int typeId) {
        Intent intent = new Intent(requireActivity(), DetailsActivity.class);
        intent.putExtra("idFragment", MyConstants.DETAIL_INFO_DATA);
        intent.putExtra("DETAIL_DATA_ID", typeId);
        startActivity(intent);
    }
}
