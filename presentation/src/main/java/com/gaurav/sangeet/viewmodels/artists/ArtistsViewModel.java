package com.gaurav.sangeet.viewmodels.artists;

import android.arch.lifecycle.MutableLiveData;

import com.gaurav.domain.usecases.actions.PlayArtistAction;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.domain.usecases.interfaces.FetchUseCases;
import com.gaurav.sangeet.di.Injector;
import com.gaurav.sangeet.viewmodels.BaseViewModel;
import com.gaurav.sangeet.views.interfaces.ArtistsView;
import com.gaurav.sangeet.views.uievents.artists.ArtistItemClickUIEvent;
import com.gaurav.sangeet.views.viewstates.ArtistsViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class ArtistsViewModel extends BaseViewModel {
    @Inject
    FetchUseCases fetchUseCases;
    @Inject
    CommandUseCases commandUseCases;
    private ArtistsView artistsView;
    private MutableLiveData<ArtistsViewState> state;

    public ArtistsViewModel(ArtistsView artistsView) {
        Injector.get().inject(this);
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
        compositeDisposable.add(artistsView.getUIEvents().map(artistsViewUIEvent -> {
            if (artistsViewUIEvent instanceof ArtistItemClickUIEvent) {
                return new PlayArtistAction(
                        ((ArtistItemClickUIEvent) artistsViewUIEvent).getArtist(),
                        ((ArtistItemClickUIEvent) artistsViewUIEvent).getArtist().songSet.first());
            } else {
                return new PlayArtistAction(null, null);
            }
        }).subscribe(playArtistAction -> commandUseCases.actionSubject().onNext(playArtistAction)));
    }

    public MutableLiveData<ArtistsViewState> getState() {
        return state;
    }
}
