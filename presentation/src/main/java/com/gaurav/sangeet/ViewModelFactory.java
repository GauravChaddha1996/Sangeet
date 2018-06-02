package com.gaurav.sangeet;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.gaurav.domain.interfaces.MusicInteractor;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    MusicInteractor musicInteractor;

    public ViewModelFactory(MusicInteractor musicInteractor) {
        this.musicInteractor = musicInteractor;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == HomeViewModel.class) {
            return (T) new HomeViewModel(musicInteractor);
        }
        return super.create(modelClass);
    }
}
