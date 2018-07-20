package com.gaurav.sangeet.viewModels.artists;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.domain.usecases.interfaces.FetchUseCases;
import com.gaurav.sangeet.views.interfaces.ArtistsView;

public class ArtistsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private ArtistsView artistsView;

    public ArtistsViewModelFactory(ArtistsView artistsView) {
        this.artistsView = artistsView;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == ArtistsViewModel.class) {
            return (T) new ArtistsViewModel(artistsView);
        }
        return super.create(modelClass);
    }
}
