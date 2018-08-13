package com.gaurav.sangeet.viewmodels.artistdetails;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.gaurav.sangeet.views.interfaces.ArtistDetailView;

public class ArtistDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    long artistId;

    public ArtistDetailViewModelFactory(long artistId) {
        this.artistId = artistId;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == ArtistDetailViewModel.class) {
            return (T) new ArtistDetailViewModel(artistId);
        }
        return super.create(modelClass);
    }
}
