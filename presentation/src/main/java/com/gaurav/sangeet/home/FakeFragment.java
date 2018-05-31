package com.gaurav.sangeet.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaurav.domain.models.Song;
import com.gaurav.sangeet.R;

import io.reactivex.android.schedulers.AndroidSchedulers;

@SuppressLint("ValidFragment")
public class FakeFragment extends Fragment {
    HomeViewModel viewModel;

    public FakeFragment(HomeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fake_frag, container, false);
    }

    @Override
    @SuppressLint("CheckResult")
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        viewModel.getAllSongs()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> {
                    RVAdapter rvAdapter = new RVAdapter(songs, getLayoutInflater(),
                            new RVAdapter.OnClickListener() {
                                @Override
                                public void onClick(Song song) {
                                    viewModel.play(song);
                                }
                            });
                    recyclerView.setAdapter(rvAdapter);

                });
    }

}
