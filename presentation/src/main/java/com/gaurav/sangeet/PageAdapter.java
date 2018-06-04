package com.gaurav.sangeet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.FetchUseCases;
import com.gaurav.sangeet.views.implementations.albums.AlbumsViewImpl;
import com.gaurav.sangeet.views.implementations.artists.ArtistsViewImpl;
import com.gaurav.sangeet.views.implementations.playlists.PlaylistsViewImpl;
import com.gaurav.sangeet.views.implementations.songs.SongsViewImpl;

public class PageAdapter extends FragmentStatePagerAdapter {

    private FetchUseCases fetchUseCases;
    private CommandUseCases commandUseCases;

    public PageAdapter(FragmentManager fm, FetchUseCases fetchUseCases, CommandUseCases commandUseCases) {
        super(fm);
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SongsViewImpl(fetchUseCases, commandUseCases);
            case 1:
                return new AlbumsViewImpl(fetchUseCases, commandUseCases);
            case 2:
                return new ArtistsViewImpl(fetchUseCases, commandUseCases);
            case 3:
                return new PlaylistsViewImpl(fetchUseCases, commandUseCases);
            default:
                return new SongsViewImpl(fetchUseCases, commandUseCases);
        }

    }

    @Override
    public int getCount() {
        return 4;
    }
}
