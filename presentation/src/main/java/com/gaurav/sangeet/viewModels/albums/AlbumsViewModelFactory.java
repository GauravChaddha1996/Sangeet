package com.gaurav.sangeet.viewModels.albums;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.domain.usecases.interfaces.FetchUseCases;
import com.gaurav.sangeet.views.interfaces.AlbumsView;

public class AlbumsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    FetchUseCases fetchUseCases;
    CommandUseCases commandUseCases;
    AlbumsView albumsView;

    public AlbumsViewModelFactory(FetchUseCases fetchUseCases, CommandUseCases commandUseCases,
                                  AlbumsView albumsView) {
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
        this.albumsView = albumsView;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == AlbumsViewModel.class) {
            return (T) new AlbumsViewModel(fetchUseCases, commandUseCases, albumsView);
        }
        return super.create(modelClass);
    }
}
