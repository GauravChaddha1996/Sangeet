package com.gaurav.sangeet.di;

import com.gaurav.sangeet.MusicApplication;
import com.gaurav.sangeet.viewmodels.albumdetails.AlbumDetailViewModel;
import com.gaurav.sangeet.viewmodels.albums.AlbumsViewModel;
import com.gaurav.sangeet.viewmodels.artistdetails.ArtistDetailViewModel;
import com.gaurav.sangeet.viewmodels.artists.ArtistsViewModel;
import com.gaurav.sangeet.viewmodels.bottomsheet.BottomSheetViewModel;
import com.gaurav.sangeet.viewmodels.songs.SongsViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {SingletonModule.class, ContentResolverModule.class})
@Singleton
public interface SingletonComponent {
    void inject(MusicApplication musicApplication);

    void inject(BottomSheetViewModel viewModel);

    void inject(SongsViewModel songsViewModel);

    void inject(AlbumsViewModel albumsViewModel);

    void inject(ArtistsViewModel artistsViewModel);

    void inject(AlbumDetailViewModel albumDetailViewModel);

    void inject(ArtistDetailViewModel artistDetailViewModel);

}
