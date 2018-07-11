package com.gaurav.sangeet.views.interfaces;

import com.gaurav.sangeet.views.uiEvents.bottomSheet.BottomSheetUIEvent;
import com.gaurav.sangeet.views.viewStates.BottomSheetViewState;

import io.reactivex.subjects.PublishSubject;

public interface BottomSheetView {

    void render(BottomSheetViewState viewState);

    PublishSubject<BottomSheetUIEvent> getUIEvents();
}
