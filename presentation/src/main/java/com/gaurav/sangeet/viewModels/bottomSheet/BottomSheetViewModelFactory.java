package com.gaurav.sangeet.viewModels.bottomSheet;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.view.View;

import com.gaurav.domain.interfaces.MusicStateManager;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.sangeet.views.interfaces.BottomSheetView;

public class BottomSheetViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private BottomSheetView bottomSheetView;
    private CommandUseCases commandUseCases;
    private MusicStateManager musicStateManager;
    private View.OnClickListener onClickListener;

    public BottomSheetViewModelFactory(BottomSheetView bottomSheetView,
                                       CommandUseCases commandUseCases,
                                       MusicStateManager musicStateManager,
                                       View.OnClickListener onClickListener) {
        this.bottomSheetView = bottomSheetView;
        this.commandUseCases = commandUseCases;
        this.musicStateManager = musicStateManager;
        this.onClickListener = onClickListener;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == BottomSheetViewModel.class) {
            return (T) new BottomSheetViewModel(bottomSheetView, commandUseCases, musicStateManager,
                    onClickListener);
        }
        return super.create(modelClass);
    }
}
