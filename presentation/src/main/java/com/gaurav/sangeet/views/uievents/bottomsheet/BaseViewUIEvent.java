package com.gaurav.sangeet.views.uievents.bottomsheet;

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
