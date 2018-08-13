package com.gaurav.sangeet.viewmodels;

import android.arch.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseViewModel extends ViewModel {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected abstract void bindIntents();

    @Override
    protected void onCleared() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onCleared();
    }
}
