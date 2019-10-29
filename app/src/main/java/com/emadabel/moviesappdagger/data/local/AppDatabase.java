package com.emadabel.moviesappdagger.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {MovieDao.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static String getDATABASE_NAME() {
        return DATABASE_NAME;
    }

    private static final String DATABASE_NAME = "moviesapp_database";

    public abstract MovieDao MovieDao();
}
