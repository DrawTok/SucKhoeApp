package com.draw.suckhoe.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.draw.suckhoe.databinding.ItemFragmentBinding;
import com.draw.suckhoe.factories.ViewModelFactory;
import com.draw.suckhoe.model.Titles;
import com.draw.suckhoe.view.adapter.ContentAdapter;
import com.draw.suckhoe.view.viewModels.ItemViewModel;



public class ItemFragment extends Fragment {

    ItemFragmentBinding binding;

    ItemViewModel viewModel;
    ContentAdapter adapter;
    int titleId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = ItemFragmentBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(ItemViewModel.class);

        Bundle bundle = getArguments();
        if(bundle != null)
        {
            Titles titles = (Titles) bundle.get("TITLE_DATA");
            if(titles != null)
            {
                titleId = titles.getTitleId();
                Glide.with(requireActivity())
                        .load(titles.getImageUrl().trim()).into(binding.imvMenu);
                binding.tvTitleContent.setText(titles.getTitleName());
            }
        }

        adapter = new ContentAdapter();
        viewModel.getAllData(titleId);
        viewModel.getLiveData().observe(getViewLifecycleOwner(), items -> adapter.setList(items));
        binding.recycleViewItem.setAdapter(adapter);
        binding.recycleViewItem.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycleViewItem.setHasFixedSize(true);

        return binding.getRoot();
    }
}
