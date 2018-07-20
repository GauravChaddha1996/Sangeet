package com.gaurav.sangeet.di;

import android.content.ContentResolver;

import dagger.Module;
import dagger.Provides;

@Module
public class ContentResolverModule {

    private ContentResolver contentResolver;

    public ContentResolverModule(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Provides
    public ContentResolver getContentResolver() {
        return contentResolver;
    }
}
