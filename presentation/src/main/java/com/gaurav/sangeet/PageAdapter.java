package com.gaurav.sangeet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gaurav.sangeet.views.songs.SongView;

public class PageAdapter extends FragmentStatePagerAdapter {

    HomeViewModel viewModel;

    public PageAdapter(FragmentManager fm, HomeViewModel viewModel) {
        super(fm);
        this.viewModel = viewModel;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SongView(viewModel);
            case 1:
                return new SongView(viewModel);
            case 2:
                return new SongView(viewModel);
            case 3:
                return new SongView(viewModel);
            case 4:
                return new SongView(viewModel);
            default:
                return new SongView(viewModel);
        }

    }

    @Override
    public int getCount() {
        return 5;
    }
}
