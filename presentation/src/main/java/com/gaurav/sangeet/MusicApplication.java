package com.gaurav.sangeet;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.interfaces.MusicService;
import com.gaurav.domain.interfaces.MusicStateManager;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.domain.usecases.interfaces.FetchUseCases;
import com.gaurav.sangeet.di.ContentResolverModule;
import com.gaurav.sangeet.di.DaggerSingletonComponent;
import com.gaurav.sangeet.di.SingletonComponent;
import com.gaurav.sangeet.di.SingletonModule;
import com.gaurav.service.MusicServiceImpl;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MusicApplication extends Application {

    private static MusicApplication instance = null;
    private SingletonComponent component;

    @Inject
    MusicRepository musicRepository;
    @Inject
    MusicStateManager musicStateManager;
    @Inject
    FetchUseCases fetchUseCases;
    @Inject
    CommandUseCases commandUseCases;
    private MusicService musicService;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        component = DaggerSingletonComponent.builder()
                .contentResolverModule(new ContentResolverModule(getContentResolver()))
                .singletonModule(new SingletonModule())
                .build();
        component.inject(this);
    }

    public Completable init() {
        return Completable.concatArray(getServiceBindingCompletable(),
                musicRepository.init().andThen(musicStateManager.init()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable getServiceBindingCompletable() {
        return Completable.create(emitter -> bindService(new Intent(this, MusicServiceImpl.class),
                new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        musicService = ((MusicServiceImpl.MusicServiceBinder) service).getService();
                        musicStateManager.attachCommandUseCases(commandUseCases);
                        musicStateManager.attachMusicService(musicService);
                        commandUseCases.attachMusicService(musicService);
                        emitter.onComplete();
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        musicStateManager.detachCommandUseCases();
                        musicStateManager.detachMusicService();
                        commandUseCases.detachMusicService();
                    }
                }, BIND_AUTO_CREATE)
        ).subscribeOn(Schedulers.io());
    }

    public static MusicApplication getInstance() {
        return instance;
    }

    public SingletonComponent getComponent() {
        return component;
    }
}
