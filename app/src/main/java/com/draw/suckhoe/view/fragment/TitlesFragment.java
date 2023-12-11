package com.draw.suckhoe.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.draw.suckhoe.R;
import com.draw.suckhoe.databinding.TitlesFragmentBinding;
import com.draw.suckhoe.factories.ViewModelFactory;
import com.draw.suckhoe.model.Titles;
import com.draw.suckhoe.model.Types;
import com.draw.suckhoe.myInterface.OnClickItemListener;
import com.draw.suckhoe.view.activity.DetailsActivity;
import com.draw.suckhoe.view.adapter.InfoDetailsAdapter;
import com.draw.suckhoe.view.viewModels.TitleViewModel;

import java.util.ArrayList;
import java.util.List;

public class TitlesFragment extends Fragment implements OnClickItemListener {

    TitlesFragmentBinding binding;
    TitleViewModel viewModel;
    InfoDetailsAdapter adapter;
    DetailsActivity activity;

    List<Titles> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = TitlesFragmentBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(TitleViewModel.class);

        activity = (DetailsActivity) getActivity();
        if(activity != null)
        {
            activity.findViewById(R.id.tvAddNewRecord).setVisibility(View.GONE);
        }

        Bundle bundle = getArguments();
       if(bundle != null)
       {
           int typeId = bundle.getInt("TYPE_ID", -1);
           adapter = new InfoDetailsAdapter();
           viewModel.getAllData(typeId);
           viewModel.getLiveData().observe(getViewLifecycleOwner(), titles -> {
               list = titles;
               adapter.setList(titles, getContext());
           });
           adapter.setListener(this);
           binding.recycleItem.setAdapter(adapter);
           binding.recycleItem.setLayoutManager(new LinearLayoutManager(getContext()));
           binding.recycleItem.setHasFixedSize(true);
       }

        return binding.getRoot();
    }

    @Override
    public void onClickItemListener(int pos) {
        ItemFragment itemFragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("TITLE_DATA", list.get(pos));
        itemFragment.setArguments(bundle);
        activity.replaceFragment(itemFragment);
    }
}
