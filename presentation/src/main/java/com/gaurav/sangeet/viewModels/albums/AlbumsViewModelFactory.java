package com.gaurav.sangeet.viewModels.albums;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.gaurav.sangeet.views.interfaces.AlbumsView;

public class AlbumsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private AlbumsView albumsView;

    public AlbumsViewModelFactory(AlbumsView albumsView) {
        this.albumsView = albumsView;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == AlbumsViewModel.class) {
            return (T) new AlbumsViewModel(albumsView);
        }
        return super.create(modelClass);
    }
}
