package com.gaurav.sangeet.views.uiEvents.bottomSheet;

import android.view.View;

public class BaseViewUIEvent implements BottomSheetUIEvent {
    private View baseView;

    public BaseViewUIEvent(View v) {
        this.baseView = v;
    }

    public View getBaseView() {
        return baseView;
    }
}
