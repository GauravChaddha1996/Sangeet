package com.gaurav.sangeet.viewModels.bottomSheet;

import android.arch.lifecycle.MutableLiveData;
import android.view.View;

import com.gaurav.domain.interfaces.MusicStateManager;
import com.gaurav.domain.musicState.MusicState;
import com.gaurav.domain.usecases.actions.NextSongAction;
import com.gaurav.domain.usecases.actions.PauseSongAction;
import com.gaurav.domain.usecases.actions.PrevSongAction;
import com.gaurav.domain.usecases.actions.RepeatAction;
import com.gaurav.domain.usecases.actions.ResumeSongAction;
import com.gaurav.domain.usecases.actions.ShuffleAction;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.sangeet.viewModels.BaseViewModel;
import com.gaurav.sangeet.views.interfaces.BottomSheetView;
import com.gaurav.sangeet.views.uiEvents.bottomSheet.BaseViewUIEvent;
import com.gaurav.sangeet.views.uiEvents.bottomSheet.NextUIEvent;
import com.gaurav.sangeet.views.uiEvents.bottomSheet.PlayPauseUIEvent;
import com.gaurav.sangeet.views.uiEvents.bottomSheet.PrevUIEvent;
import com.gaurav.sangeet.views.uiEvents.bottomSheet.RepeatUIEvent;
import com.gaurav.sangeet.views.uiEvents.bottomSheet.ShuffleUIEvent;
import com.gaurav.sangeet.views.viewStates.BottomSheetViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class BottomSheetViewModel extends BaseViewModel {

    private BottomSheetView bottomSheetView;
    private CommandUseCases commandUseCases;
    private MusicStateManager musicStateManager;
    private View.OnClickListener onClickListener;

    private MutableLiveData<BottomSheetViewState> viewState;

    // helper vars
    private MusicState storedMusicState;

    public BottomSheetViewModel(BottomSheetView bottomSheetView, CommandUseCases commandUseCases,
                                MusicStateManager musicStateManager,
                                View.OnClickListener onClickListener) {
        this.bottomSheetView = bottomSheetView;
        this.commandUseCases = commandUseCases;
        this.musicStateManager = musicStateManager;
        this.onClickListener = onClickListener;

        bindIntents();

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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void bindIntents() {
        compositeDisposable.add(bottomSheetView.getUIEvents()
                .subscribe(bottomSheetUIEvent -> {
                    if (bottomSheetUIEvent instanceof BaseViewUIEvent) {
                        onClickListener.onClick(((BaseViewUIEvent) bottomSheetUIEvent).getBaseView());
                    } else if (bottomSheetUIEvent instanceof PlayPauseUIEvent) {
                        commandUseCases.actionSubject()
                                .onNext(viewState.getValue().getMusicState().isPlaying() ? new PauseSongAction() :
                                        new ResumeSongAction());
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
