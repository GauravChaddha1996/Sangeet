package com.gaurav.sangeet.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gaurav.sangeet.views.implementations.albums.AlbumsViewImpl;
import com.gaurav.sangeet.views.implementations.artists.ArtistsViewImpl;
import com.gaurav.sangeet.views.implementations.songs.SongsViewImpl;

public class PageAdapter extends FragmentStatePagerAdapter {

    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SongsViewImpl();
            case 1:
                return new AlbumsViewImpl();
            case 2:
                return new ArtistsViewImpl();
            case 3:
                return new SongsViewImpl();
            default:
                return new SongsViewImpl();
        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Songs";
            case 1:
                return "Albums";
            case 2:
                return "Artists";
            case 3:
                return "Favorites";
            default:
                return "Default";
        }
    }
}
