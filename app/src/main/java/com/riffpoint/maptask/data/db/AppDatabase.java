package com.riffpoint.maptask.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.riffpoint.maptask.data.dao.MapDao;
import com.riffpoint.maptask.data.entity.MapEntity;
import com.riffpoint.maptask.utils.AppConstants;


/**
 * Created by Fedchuk Maxim on 2018-10-06.
 * Copyright (c) 2018 Fedchuk Maxim All rights reserved.
 */
@Database(entities = {MapEntity.class}, version = AppDatabase.VERSION, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    static final int VERSION = 1;
    private static final String DB_NAME = AppConstants.DB_NAME;
    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                DB_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    public abstract MapDao getMapDao();
}
