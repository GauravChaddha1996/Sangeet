package com.gaurav.sangeet.viewmodels.bottomsheet;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.view.View;

import com.gaurav.sangeet.views.interfaces.BottomSheetView;

public class BottomSheetViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private BottomSheetView bottomSheetView;
    private View.OnClickListener onClickListener;

    public BottomSheetViewModelFactory(BottomSheetView bottomSheetView,
                                       View.OnClickListener onClickListener) {
        this.bottomSheetView = bottomSheetView;
        this.onClickListener = onClickListener;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == BottomSheetViewModel.class) {
            return (T) new BottomSheetViewModel(bottomSheetView, onClickListener);
        }
        return super.create(modelClass);
    }
}
