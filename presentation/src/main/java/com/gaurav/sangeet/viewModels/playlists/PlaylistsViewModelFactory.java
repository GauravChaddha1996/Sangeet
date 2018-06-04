package com.gaurav.sangeet.viewModels.playlists;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.FetchUseCases;
import com.gaurav.sangeet.views.interfaces.PlaylistsView;

public class PlaylistsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    FetchUseCases fetchUseCases;
    CommandUseCases commandUseCases;
    PlaylistsView playlistsView;

    public PlaylistsViewModelFactory(FetchUseCases fetchUseCases, CommandUseCases commandUseCases,
                                     PlaylistsView playlistsView) {
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
        this.playlistsView = playlistsView;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == PlaylistsViewModel.class) {
            return (T) new PlaylistsViewModel(fetchUseCases, commandUseCases, playlistsView);
        }
        return super.create(modelClass);
    }
}
