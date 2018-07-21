package com.gaurav.sangeet.viewmodels.albumdetails;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.gaurav.sangeet.views.interfaces.AlbumDetailView;

public class AlbumDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    AlbumDetailView albumDetailView;
    long albumId;

    public AlbumDetailViewModelFactory(AlbumDetailView albumDetailView, long albumId) {
        this.albumDetailView = albumDetailView;
        this.albumId = albumId;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == AlbumDetailViewModel.class) {
            return (T) new AlbumDetailViewModel(albumDetailView, albumId);
        }
        return super.create(modelClass);
    }
}
