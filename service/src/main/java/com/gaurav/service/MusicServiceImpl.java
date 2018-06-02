package com.gaurav.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.gaurav.domain.interfaces.MusicInteractor;
import com.gaurav.domain.interfaces.MusicService;

import java.io.IOException;

import io.reactivex.Completable;

/*
 * Todos for tomorrow:
 *
 * 1. Make this service actually a service and let it start with appropriate place and handle itself well
 * 2. make communication to this service clear. - bind in application init
 * 3. Handle illegal states in media player
 *
 * 4. [DONE] Clean splash activity animation and activity code itself
 * 5. [DONE] clean musicpplication class
 * 6. clean home activity
 * 7. clean viewmodel and rvadapter
 * 8. clean fakefragment to give actions to viewmodel instead of commands
 *
 * 9. better state reducer code
 * 10. clean music interactor impl
 *
 * 11. make presentation show MusicState data - observe music state
 * 12. implement music state save
 * 13. add callback listeners for updating duration
 *
 * 13. add other functionality like play pause next  - view queue
 * 14. add play for album, artist and stuff
 * 15. implement addToQueue for people
 * 16. implement playNext
 * */
public class MusicServiceImpl extends Service implements MusicService {

    MusicInteractor musicInteractor;
    MediaPlayer mediaPlayer;

    MusicServiceBinder binder;

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
    public void attachMusicInteractor(MusicInteractor musicInteractor) {
        this.musicInteractor = musicInteractor;
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
