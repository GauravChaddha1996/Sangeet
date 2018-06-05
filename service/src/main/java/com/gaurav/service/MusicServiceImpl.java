package com.gaurav.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.gaurav.domain.interfaces.MusicService;
import com.gaurav.domain.usecases.CommandUseCases;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/*
 * Todos for tomorrow:
 *
 * [DONE] Make this service actually a service and let it start with appropriate place and handle itself well
 * [DONE] make communication to this service clear. - bind in application init
 * [DONE] Handle illegal states in media player
 *
 * [DONE] Clean splash activity animation and activity code itself
 * [DONE] clean musicpplication class
 * [DONE] clean home activity
 * [DONE] clean viewmodel and rvadapter
 * [DONE] clean fakefragment
 *
 * [DONE] Give each view it's own model.
 * [DONE] To give events to viewmodel instead of commands and actions flowing out from view models
 * [DONE] remove the useless data model transformations

 * [Done] show albums and artists and playlists
 * [Done] handle their clicking and stuff and playing

 * [DONE] better state reducer code
 * [DONE] clean music interactor impl
 *
 * [DONE] make presentation show MusicState data - observe music state
 * [DONE] implement music state save
 * [DONE] add callback listeners for updating duration
 * [DONE] clean off all disposables
 *
 *
 * [] add other functionality like play pause next  - play next - and queue actions
 *
 * [] Presentation layer + notification
 * [] handle to-dos
 * [] handle corner cases from trello
 * */
public class MusicServiceImpl extends Service implements MusicService {

    private CommandUseCases commandUseCases;
    private MediaPlayer mediaPlayer;
    private MusicServiceBinder binder;
    private Disposable progressDisposable;
    private Disposable songToPlayDisposable;

    private Emitter<Integer> progressEmitter;
    private Observable<Integer> progressObservable;

    private Emitter<Boolean> songCompleteEmitter;
    private Observable<Boolean> songCompleteObservable;


    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        prepareObservablesAndEmitters();
        startForeground(101, getNotification());
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MusicServiceImpl", "Binding ... ");
        if (binder == null) {
            binder = new MusicServiceBinder();
        }
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("MusicServiceImpl", "Unbinding ... ");
        binder = null;
        return false;
    }

    @Override
    public void attachCommandUseCases(CommandUseCases commandUseCases) {
        this.commandUseCases = commandUseCases;
        songToPlayDisposable = commandUseCases.observeSongToPlay()
                .subscribe(song -> this.play(song.data));
    }

    @Override
    public void detachCommandUseCases() {
        if (songToPlayDisposable != null && !songToPlayDisposable.isDisposed()) {
            songToPlayDisposable.dispose();
        }
        this.commandUseCases = null;
    }

    @Override
    public Observable<Integer> observeProgress() {
        return progressObservable;
    }

    @Override
    public Observable<Boolean> observeSongCompletion() {
        return songCompleteObservable;
    }

    @Override
    public void play(String path) {
        mediaPlayer.reset();
        if (progressDisposable != null && !progressDisposable.isDisposed()) {
            progressDisposable.dispose();
        }
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(__ -> songCompleteEmitter.onNext(true));
            progressDisposable = Observable.interval(1, TimeUnit.SECONDS)
                    .doOnNext(aLong -> progressEmitter.onNext(mediaPlayer.getCurrentPosition()))
                    .subscribe();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        if (progressDisposable != null && !progressDisposable.isDisposed()) {
            progressDisposable.dispose();
        }
        super.onDestroy();
    }

    /*
     * Helper functions and classes
     * */

    private void prepareObservablesAndEmitters() {
        progressObservable = Observable.create(emitter -> this.progressEmitter = emitter);
        songCompleteObservable = Observable.create(emitter -> this.songCompleteEmitter = emitter);
    }

    private Notification getNotification() {
        return new Notification.Builder(this)
                .setSmallIcon(R.drawable.notif_icon)
                .setContentTitle("Sangeet app")
                .setContentText("Sangeet is playing").build();

    }

    public class MusicServiceBinder extends Binder {
        public MusicService getService() {
            return MusicServiceImpl.this;
        }
    }
}
