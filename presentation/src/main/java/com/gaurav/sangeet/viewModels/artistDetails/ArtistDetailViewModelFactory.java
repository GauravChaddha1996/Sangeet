package com.gaurav.sangeet.viewModels.artistDetails;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.domain.usecases.interfaces.FetchUseCases;
import com.gaurav.sangeet.views.interfaces.ArtistDetailView;

public class ArtistDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    FetchUseCases fetchUseCases;
    CommandUseCases commandUseCases;
    ArtistDetailView artistDetailView;
    long artistId;

    public ArtistDetailViewModelFactory(FetchUseCases fetchUseCases, CommandUseCases commandUseCases,
                                        ArtistDetailView artistDetailView, long artistId) {
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
        this.artistDetailView = artistDetailView;
        this.artistId = artistId;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == ArtistDetailViewModel.class) {
            return (T) new ArtistDetailViewModel(fetchUseCases, commandUseCases, artistDetailView, artistId);
        }
        return super.create(modelClass);
    }
}
