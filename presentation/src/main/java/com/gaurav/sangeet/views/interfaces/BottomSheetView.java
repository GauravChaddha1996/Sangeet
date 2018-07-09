package com.gaurav.sangeet.views.interfaces;

import com.gaurav.domain.MusicState;
import com.gaurav.domain.models.Song;
import com.gaurav.sangeet.views.uiEvents.bottomSheet.BottomSheetUIEvent;
import com.gaurav.sangeet.views.uiEvents.songs.SongViewUIEvent;
import com.gaurav.sangeet.views.viewStates.BottomSheetViewState;
import com.gaurav.sangeet.views.viewStates.SongsViewState;

import io.reactivex.subjects.PublishSubject;

public interface BottomSheetView {

    void render(BottomSheetViewState viewState);

    PublishSubject<BottomSheetUIEvent> getUIEvents();
}
