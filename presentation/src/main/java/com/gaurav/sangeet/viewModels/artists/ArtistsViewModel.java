package com.gaurav.sangeet.viewModels.artists;

import android.arch.lifecycle.MutableLiveData;

import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.FetchUseCases;
import com.gaurav.sangeet.viewModels.BaseViewModel;
import com.gaurav.sangeet.views.implementations.artists.ArtistsViewState;
import com.gaurav.sangeet.views.interfaces.ArtistsView;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class ArtistsViewModel extends BaseViewModel {
    private FetchUseCases fetchUseCases;
    private CommandUseCases commandUseCases;
    private ArtistsView artistsView;
    private MutableLiveData<ArtistsViewState> state;

    public ArtistsViewModel(FetchUseCases fetchUseCases, CommandUseCases commandUseCases, ArtistsView artistsView) {
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
        this.artistsView = artistsView;

        bindIntents();

        state = new MutableLiveData<>();
        compositeDisposable.add(
                fetchUseCases.getAllArtists()
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(__ -> state.setValue(new ArtistsViewState.Loading()))
                        .subscribe(artists -> state.setValue(new ArtistsViewState.Result(artists)),
                                throwable -> state.setValue(new ArtistsViewState.Error())));
    }

    @Override
    public void bindIntents() {
        compositeDisposable.add(artistsView.playIntent().subscribe(commandUseCases::play));
    }

    public MutableLiveData<ArtistsViewState> getState() {
        return state;
    }
}
