package com.gaurav.sangeet;

import android.app.Application;

import com.gaurav.data.MusicDatabase;
import com.gaurav.data.MusicRepositoryImpl;
import com.gaurav.domain.MusicInteractorImpl;
import com.gaurav.domain.interfaces.MusicInteractor;
import com.gaurav.domain.interfaces.MusicRepository;

import io.reactivex.Completable;

public class MusicApplication extends Application {

    MusicDatabase musicDatabase;
    MusicRepository musicRepository;
    MusicInteractor musicInteractor;

    @Override
    public void onCreate() {
        super.onCreate();
//        musicDatabase =
        musicRepository = new MusicRepositoryImpl(getContentResolver(),
                getSharedPreferences("sangeet", MODE_PRIVATE),
                musicDatabase);
        musicInteractor = new MusicInteractorImpl(musicRepository);
    }

    public Completable init() {
        return musicRepository.init();
    }
}
