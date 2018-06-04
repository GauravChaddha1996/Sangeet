package com.gaurav.sangeet.viewModels.artists;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.FetchUseCases;
import com.gaurav.sangeet.views.interfaces.ArtistsView;

public class ArtistsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    FetchUseCases fetchUseCases;
    CommandUseCases commandUseCases;
    ArtistsView artistsView;

    public ArtistsViewModelFactory(FetchUseCases fetchUseCases, CommandUseCases commandUseCases,
                                   ArtistsView artistsView) {
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
        this.artistsView = artistsView;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == ArtistsViewModel.class) {
            return (T) new ArtistsViewModel(fetchUseCases, commandUseCases, artistsView);
        }
        return super.create(modelClass);
    }
}
