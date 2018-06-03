package com.gaurav.sangeet.viewModels.song;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.FetchUseCases;
import com.gaurav.sangeet.views.interfaces.SongView;

public class SongViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    FetchUseCases fetchUseCases;
    CommandUseCases commandUseCases;
    SongView songView;

    public SongViewModelFactory(FetchUseCases fetchUseCases, CommandUseCases commandUseCases,
                                SongView songView) {
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
        this.songView = songView;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == SongViewModel.class) {
            return (T) new SongViewModel(fetchUseCases, commandUseCases, songView);
        }
        return super.create(modelClass);
    }
}
