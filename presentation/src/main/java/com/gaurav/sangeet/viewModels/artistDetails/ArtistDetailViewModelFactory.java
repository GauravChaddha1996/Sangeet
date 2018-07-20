package com.gaurav.sangeet.viewModels.artistDetails;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.domain.usecases.interfaces.FetchUseCases;
import com.gaurav.sangeet.views.interfaces.ArtistDetailView;

public class ArtistDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    ArtistDetailView artistDetailView;
    long artistId;

    public ArtistDetailViewModelFactory( ArtistDetailView artistDetailView, long artistId) {
        this.artistDetailView = artistDetailView;
        this.artistId = artistId;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == ArtistDetailViewModel.class) {
            return (T) new ArtistDetailViewModel(artistDetailView, artistId);
        }
        return super.create(modelClass);
    }
}
