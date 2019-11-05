package com.emadabel.moviesappdagger.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {MovieEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "moviesapp_database";

    public static String getDATABASE_NAME() {
        return DATABASE_NAME;
    }

    public abstract MovieDao MovieDao();
}
