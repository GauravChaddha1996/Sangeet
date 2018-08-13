package com.gaurav.sangeet.viewmodels.bottomsheet;

import android.arch.lifecycle.MutableLiveData;
import android.view.View;

import com.gaurav.domain.interfaces.MusicStateManager;
import com.gaurav.domain.musicstate.MusicState;
import com.gaurav.domain.usecases.actions.NextSongAction;
import com.gaurav.domain.usecases.actions.PauseSongAction;
import com.gaurav.domain.usecases.actions.PrevSongAction;
import com.gaurav.domain.usecases.actions.RepeatAction;
import com.gaurav.domain.usecases.actions.ResumeSongAction;
import com.gaurav.domain.usecases.actions.ShuffleAction;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.sangeet.di.Injector;
import com.gaurav.sangeet.viewmodels.BaseViewModel;
import com.gaurav.sangeet.views.interfaces.BottomSheetView;
import com.gaurav.sangeet.views.uievents.bottomsheet.BaseViewUIEvent;
import com.gaurav.sangeet.views.uievents.bottomsheet.NextUIEvent;
import com.gaurav.sangeet.views.uievents.bottomsheet.PlayPauseUIEvent;
import com.gaurav.sangeet.views.uievents.bottomsheet.PrevUIEvent;
import com.gaurav.sangeet.views.uievents.bottomsheet.RepeatUIEvent;
import com.gaurav.sangeet.views.uievents.bottomsheet.ShuffleUIEvent;
import com.gaurav.sangeet.views.viewstates.BottomSheetViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class BottomSheetViewModel extends BaseViewModel {

    private BottomSheetView bottomSheetView;
    @Inject
    CommandUseCases commandUseCases;
    @Inject
    MusicStateManager musicStateManager;
    private View.OnClickListener onClickListener;

    private MutableLiveData<BottomSheetViewState> viewState;

    // helper vars
    private MusicState storedMusicState;

    public BottomSheetViewModel() {
        Injector.get().inject(this);

        viewState = new MutableLiveData<>();
        compositeDisposable.add(musicStateManager.observeMusicState()
                .observeOn(AndroidSchedulers.mainThread())
                .map(musicState -> {
                    if (viewState.getValue() != null &&
                            viewState.getValue().getMusicState().getCurrentSong() != null) {
                        storedMusicState = viewState.getValue().getMusicState();
                        return new BottomSheetViewState(musicState,
                                !storedMusicState.getCurrentSong()
                                        .equals(musicState.getCurrentSong())
                                , musicState.getCurrentSong());
                    } else {
                        return new BottomSheetViewState(musicState, true,
                                musicState.getCurrentSong());
                    }
                })
                .subscribe(viewState::setValue));
    }

    public void attachBottomSheetView(BottomSheetView bottomSheetView) {
        this.bottomSheetView = bottomSheetView;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        bindIntents();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void bindIntents() {
        compositeDisposable.add(bottomSheetView.getUIEvents()
                .subscribe(bottomSheetUIEvent -> {
                    if (bottomSheetUIEvent instanceof BaseViewUIEvent) {
                        onClickListener.onClick(
                                ((BaseViewUIEvent) bottomSheetUIEvent).getBaseView());
                    } else if (bottomSheetUIEvent instanceof PlayPauseUIEvent) {
                        commandUseCases.actionSubject()
                                .onNext(viewState.getValue().getMusicState().isPlaying() ?
                                        new PauseSongAction() : new ResumeSongAction());
                    } else if (bottomSheetUIEvent instanceof PrevUIEvent) {
                        commandUseCases.actionSubject().onNext(new PrevSongAction());
                    } else if (bottomSheetUIEvent instanceof NextUIEvent) {
                        commandUseCases.actionSubject().onNext(new NextSongAction());
                    } else if (bottomSheetUIEvent instanceof ShuffleUIEvent) {
                        commandUseCases.actionSubject().onNext(new ShuffleAction());
                    } else if (bottomSheetUIEvent instanceof RepeatUIEvent) {
                        commandUseCases.actionSubject().onNext(new RepeatAction());
                    }
                }));
    }

    public MutableLiveData<BottomSheetViewState> getViewState() {
        return viewState;
    }
}
