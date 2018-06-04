package com.gaurav.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.gaurav.domain.interfaces.MusicStateManager;
import com.gaurav.domain.interfaces.MusicService;
import com.gaurav.domain.usecases.CommandUseCases;

import java.io.IOException;

import io.reactivex.Completable;

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
 * [] make presentation show MusicState data - observe music state
 * [] implement music state save
 * [] add callback listeners for updating duration
 *
 *
 * [] add other functionality like play pause next  - play next - and queue actions
 *
 * [] Presentation layer + notification
 * */
public class MusicServiceImpl extends Service implements MusicService {

    MediaPlayer mediaPlayer;

    MusicServiceBinder binder;
    CommandUseCases commandUseCases;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
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
        this.commandUseCases =commandUseCases;
    }

    @Override
    public Completable play(String path) {
        return Completable.create(emitter -> {
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();
                emitter.onComplete();
            } catch (IOException e) {
                emitter.onError(e);
            }
        });

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
