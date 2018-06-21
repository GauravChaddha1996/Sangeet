package com.gaurav.service;

import android.app.Notification;
import android.app.PendingIntent;
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
 * Inspirations: https://www.uplabs.com/posts/daily-ui-music-player,
 *               https://www.uplabs.com/posts/dark-material-music-app-ui
 * 
 * (Implement logic as required)
 * [Done] Think of all colors from different screens required - 
 	  Color primary dark - status bar
 	  Color primary - toolbar
 	  Color accent - tab layout dot = bottom sheet button color
 	  Color primary two - bottom sheet color collapsed 
 	  Color accent two - use anywhere
 * [Done] 8 color schemes - I love coolors.co
 	  (Two accents, two primary, two dark shade - two shades down of primary)
 	  Light themes:
 	  https://coolors.co/237eaf-243687-fdfffc-ff9f1c-ce3030
 	  https://coolors.co/f87060-003971-4171af-42ad51-ffc857
 	  https://coolors.co/009ea0-ffba49-ef5b5b-4b2e46-b4b8bb
 	  https://coolors.co/dbb13b-478060-3b597b-0a0e29-efac73
 	  Dark themes:
 	  https://coolors.co/95dce6-6564db-232ed1-101d42-0d1317
 	  https://coolors.co/494949-4a314d-ff5d73-1b5299-4381c1 
 	  https://coolors.co/040f0f-248232-2ba84a-2d3a3a-fcfffc 
 	  https://coolors.co/3c1518-69140e-a44200-d58936-f2f3ae
 * [Done] Find logo Design apps 
 		Gravit
 		Figma - design and maintain  - prototype, wireframe. 
		Zeplin - design and maintain - prototype, wireframe.
 		avocode - design to measurements
 		Krita - digital paint art
 * [] Design logo - in different colors - Soundwaves - they can animate and write some name.
 * [] Lottie - animate your logo. Show it now.
 * [] Find good design apps
 * [] Home screen UI - Toolbar, TabLayout, Bottom sheet collapsed
 * [] Song item UI and animation
 * [] Home screen overall UI and animation
 * [] Bottom sheet full UI
 * [] Bottom sheet animation and menu change in toolbar
 * [] Album, artists, playlist screen UI
 * [] Album, artists, playlist screen animation
 * [] Individual album, artist, playlist screen UI
 * [] Individual album, artist, playlist screen animation
 * [] Search screen UI
 * [] Search screen Animation
 * [] Settings screen UI
 * [] Settings screen Animation
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
