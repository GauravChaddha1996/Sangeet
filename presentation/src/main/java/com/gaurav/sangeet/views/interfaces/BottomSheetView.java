package com.gaurav.sangeet.views.interfaces;

import com.gaurav.sangeet.views.uievents.bottomsheet.BottomSheetUIEvent;
import com.gaurav.sangeet.views.viewstates.BottomSheetViewState;

import io.reactivex.subjects.PublishSubject;

public interface BottomSheetView {

    void render(BottomSheetViewState viewState);

    PublishSubject<BottomSheetUIEvent> getUIEvents();
}
