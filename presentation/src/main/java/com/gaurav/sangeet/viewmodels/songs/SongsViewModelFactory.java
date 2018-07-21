package com.gaurav.sangeet.viewmodels.songs;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.gaurav.sangeet.views.interfaces.SongsView;

public class SongsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private SongsView songsView;

    public SongsViewModelFactory(SongsView songsView) {
        this.songsView = songsView;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == SongsViewModel.class) {
            return (T) new SongsViewModel(songsView);
        }
        return super.create(modelClass);
    }
}
