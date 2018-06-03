package com.gaurav.sangeet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.FetchUseCases;
import com.gaurav.sangeet.views.implementations.songs.SongViewImpl;

public class PageAdapter extends FragmentStatePagerAdapter {

    FetchUseCases fetchUseCases;
    CommandUseCases commandUseCases;

    public PageAdapter(FragmentManager fm, FetchUseCases fetchUseCases, CommandUseCases commandUseCases) {
        super(fm);
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SongViewImpl(fetchUseCases, commandUseCases);
            case 1:
                return new SongViewImpl(fetchUseCases, commandUseCases);
            case 2:
                return new SongViewImpl(fetchUseCases, commandUseCases);
            case 3:
                return new SongViewImpl(fetchUseCases, commandUseCases);
            case 4:
                return new SongViewImpl(fetchUseCases, commandUseCases);
            default:
                return new SongViewImpl(fetchUseCases, commandUseCases);
        }

    }

    @Override
    public int getCount() {
        return 5;
    }
}
