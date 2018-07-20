package com.gaurav.sangeet.di;

import com.gaurav.data.MusicRepositoryImpl;
import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.interfaces.MusicStateManager;
import com.gaurav.domain.musicState.MusicStateManagerImpl;
import com.gaurav.domain.usecases.impls.CommandUseCasesImpl;
import com.gaurav.domain.usecases.impls.FetchUseCasesImpl;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.domain.usecases.interfaces.FetchUseCases;

import dagger.Module;
import dagger.Provides;

@Module
public class SingletonModule {

    @Provides
    public MusicRepository musicRepository(MusicRepositoryImpl musicRepository) {
        return musicRepository;
    }

    @Provides
    public MusicStateManager musicStateManager(MusicStateManagerImpl musicStateManager) {
        return musicStateManager;
    }

    @Provides
    public FetchUseCases fetchUseCases(FetchUseCasesImpl fetchUseCases) {
        return fetchUseCases;
    }

    @Provides
    public CommandUseCases commandUseCases(CommandUseCasesImpl commandUseCases) {
        return commandUseCases;
    }
}
