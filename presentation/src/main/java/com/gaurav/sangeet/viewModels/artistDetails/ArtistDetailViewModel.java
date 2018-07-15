package com.gaurav.sangeet.viewModels.artistDetails;

import android.arch.lifecycle.MutableLiveData;

import com.gaurav.domain.models.Song;
import com.gaurav.domain.usecases.actions.PlayArtistAction;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.domain.usecases.interfaces.FetchUseCases;
import com.gaurav.sangeet.viewModels.BaseViewModel;
import com.gaurav.sangeet.views.interfaces.ArtistDetailView;
import com.gaurav.sangeet.views.uiEvents.artistDetails.PlayArtistDetailUIEvent;
import com.gaurav.sangeet.views.viewStates.ArtistDetailViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class ArtistDetailViewModel extends BaseViewModel {
    private FetchUseCases fetchUseCases;
    private CommandUseCases commandUseCases;
    private ArtistDetailView artistDetailView;
    private MutableLiveData<ArtistDetailViewState> state;

    public ArtistDetailViewModel(FetchUseCases fetchUseCases, CommandUseCases commandUseCases,
                                 ArtistDetailView artistDetailView, long artistId) {
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
        this.artistDetailView = artistDetailView;
        bindIntents();

        state = new MutableLiveData<>();
        compositeDisposable.add(
                fetchUseCases.getArtist(artistId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(__ -> state.setValue(new ArtistDetailViewState.Loading()))
                        .subscribe(artist -> state.setValue(new ArtistDetailViewState.Result(artist)),
                                throwable -> state.setValue(new ArtistDetailViewState.Error())));
    }

    @Override
    public void bindIntents() {
        compositeDisposable.add(artistDetailView.getUIEvents()
                .map(artistDetailUIEvent -> {
                    if (artistDetailUIEvent instanceof PlayArtistDetailUIEvent) {
                        Song song = ((PlayArtistDetailUIEvent) artistDetailUIEvent).getSong();
                        if (song == null) {
                            song = ((PlayArtistDetailUIEvent) artistDetailUIEvent).getArtist().songSet.first();
                        }
                        return new PlayArtistAction(((PlayArtistDetailUIEvent) artistDetailUIEvent).getArtist(), song);
                    }
                    return new PlayArtistAction(null, null);
                }).subscribe(playArtistAction -> commandUseCases.actionSubject()
                        .onNext(playArtistAction)));
    }

    public MutableLiveData<ArtistDetailViewState> getState() {
        return state;
    }
}
