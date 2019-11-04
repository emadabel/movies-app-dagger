package com.emadabel.moviesappdagger.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;

import com.emadabel.moviesappdagger.data.local.AppDatabase;
import com.emadabel.moviesappdagger.data.local.MovieDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {

    @Provides
    @Singleton
    AppDatabase provideDatabase(@NonNull Application application) {
        return Room.databaseBuilder(application,
                AppDatabase.class, AppDatabase.getDATABASE_NAME())
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    MovieDao provideMovieDao(@NonNull AppDatabase appDatabase) {
        return appDatabase.MovieDao();
    }
}
