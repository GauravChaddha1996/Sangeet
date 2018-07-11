package com.gaurav.sangeet.viewModels.songs;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.domain.usecases.interfaces.FetchUseCases;
import com.gaurav.sangeet.views.interfaces.SongsView;

public class SongsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    FetchUseCases fetchUseCases;
    CommandUseCases commandUseCases;
    SongsView songsView;

    public SongsViewModelFactory(FetchUseCases fetchUseCases, CommandUseCases commandUseCases,
                                 SongsView songsView) {
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
        this.songsView = songsView;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == SongsViewModel.class) {
            return (T) new SongsViewModel(fetchUseCases, commandUseCases, songsView);
        }
        return super.create(modelClass);
    }
}
