package com.gaurav.sangeet.di;

import com.gaurav.sangeet.MusicApplication;

public class Injector {
    public static SingletonComponent get() {
        return MusicApplication.getInstance().getComponent();
    }
}
