package com.gaurav.sangeet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gaurav.domain.models.Song;

import io.reactivex.disposables.Disposable;

public class SplashActivity extends AppCompatActivity {

    private Disposable initTasksDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initTasksDisposable = ((MusicApplication) getApplication())
                .init()
                .subscribe(() -> {
                    ((MusicApplication) getApplication()).musicInteractor.getAllSongs()
                            .subscribe(songList -> {
                                for (Song song : songList) {
                                    System.out.println(song.toString());
                                }
                            });

                    // change the animation boolean
                    // at animation end launch the next activity
                },Throwable::printStackTrace);
    }

    @Override
    protected void onStop() {
        if (initTasksDisposable != null && !initTasksDisposable.isDisposed()) {
            initTasksDisposable.dispose();
        }
        super.onStop();
    }
}
