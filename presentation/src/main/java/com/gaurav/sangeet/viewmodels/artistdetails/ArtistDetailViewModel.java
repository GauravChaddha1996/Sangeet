package com.gaurav.sangeet.viewmodels.artistdetails;

import android.arch.lifecycle.MutableLiveData;

import com.gaurav.domain.models.Song;
import com.gaurav.domain.usecases.actions.PlayArtistAction;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.domain.usecases.interfaces.FetchUseCases;
import com.gaurav.sangeet.di.Injector;
import com.gaurav.sangeet.viewmodels.BaseViewModel;
import com.gaurav.sangeet.views.interfaces.ArtistDetailView;
import com.gaurav.sangeet.views.uievents.artistdetails.PlayArtistDetailUIEvent;
import com.gaurav.sangeet.views.viewstates.ArtistDetailViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class ArtistDetailViewModel extends BaseViewModel {
    @Inject
    FetchUseCases fetchUseCases;
    @Inject
    CommandUseCases commandUseCases;
    private ArtistDetailView artistDetailView;
    private MutableLiveData<ArtistDetailViewState> state;

    public ArtistDetailViewModel(long artistId) {
        Injector.get().inject(this);

        state = new MutableLiveData<>();
        compositeDisposable.add(
                fetchUseCases.getArtist(artistId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(__ -> state.setValue(new ArtistDetailViewState.Loading()))
                        .subscribe(artist ->
                                        state.setValue(new ArtistDetailViewState.Result(artist)),
                                throwable -> state.setValue(new ArtistDetailViewState.Error())));
    }

    @Override
    public void bindIntents() {
        compositeDisposable.add(artistDetailView.getUIEvents()
                .map(artistDetailUIEvent -> {
                    if (artistDetailUIEvent instanceof PlayArtistDetailUIEvent) {
                        Song song = ((PlayArtistDetailUIEvent) artistDetailUIEvent).getSong();
                        if (song == null) {
                            song = ((PlayArtistDetailUIEvent) artistDetailUIEvent)
                                    .getArtist().songSet.first();
                        }
                        return new PlayArtistAction(
                                ((PlayArtistDetailUIEvent) artistDetailUIEvent).getArtist(), song);
                    }
                    return new PlayArtistAction(null, null);
                }).subscribe(playArtistAction -> commandUseCases.actionSubject()
                        .onNext(playArtistAction)));
    }

    public void attachArtistDetailView(ArtistDetailView artistDetailView) {
        this.artistDetailView = artistDetailView;
        bindIntents();
    }

    public MutableLiveData<ArtistDetailViewState> getState() {
        return state;
    }
}
