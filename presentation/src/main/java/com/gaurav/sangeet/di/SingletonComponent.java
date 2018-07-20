package com.gaurav.sangeet.di;

import com.gaurav.sangeet.MusicApplication;
import com.gaurav.sangeet.viewModels.albumDetails.AlbumDetailViewModel;
import com.gaurav.sangeet.viewModels.albums.AlbumsViewModel;
import com.gaurav.sangeet.viewModels.artistDetails.ArtistDetailViewModel;
import com.gaurav.sangeet.viewModels.artists.ArtistsViewModel;
import com.gaurav.sangeet.viewModels.bottomSheet.BottomSheetViewModel;
import com.gaurav.sangeet.viewModels.songs.SongsViewModel;

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
