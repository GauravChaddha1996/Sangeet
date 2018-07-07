package com.gaurav.sangeet;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.gaurav.data.MusicDatabase;
import com.gaurav.data.MusicRepositoryImpl;
import com.gaurav.domain.MusicStateManagerImpl;
import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.interfaces.MusicService;
import com.gaurav.domain.interfaces.MusicStateManager;
import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.FetchUseCases;
import com.gaurav.domain.usecases.impls.CommandUseCasesImpl;
import com.gaurav.domain.usecases.impls.FetchUseCasesImpl;
import com.gaurav.service.MusicServiceImpl;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MusicApplication extends Application {

    MusicDatabase musicDatabase;
    MusicRepository musicRepository;
    MusicService musicService;
    public MusicStateManager musicStateManager;
    public FetchUseCases fetchUseCases;
    public CommandUseCases commandUseCases;

    @Override
    public void onCreate() {
        super.onCreate();

        musicDatabase = Room.databaseBuilder(this,
                MusicDatabase.class, "sangeet-db").build();
        musicRepository = new MusicRepositoryImpl(getContentResolver(),
                getSharedPreferences("sangeet", MODE_PRIVATE),
                musicDatabase);
        musicStateManager = new MusicStateManagerImpl(musicRepository);
        fetchUseCases = new FetchUseCasesImpl(musicRepository);
        commandUseCases = new CommandUseCasesImpl(musicRepository, musicStateManager);
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
                        musicService.attachCommandUseCases(commandUseCases);
                        musicStateManager.attachCommandUseCases(commandUseCases);
                        musicStateManager.attachMusicService(musicService);
                        emitter.onComplete();
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        musicService.detachCommandUseCases();
                        musicStateManager.detachCommandUseCases();
                        musicStateManager.detachMusicService();
                    }
                }, BIND_AUTO_CREATE)
        ).subscribeOn(Schedulers.io());
    }
}
