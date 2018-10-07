package com.riffpoint.maptask.di.components;

import android.app.Application;
import android.content.Context;

import com.riffpoint.maptask.MapTask;
import com.riffpoint.maptask.data.db.AppDatabase;
import com.riffpoint.maptask.di.ApplicationContext;
import com.riffpoint.maptask.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Fedchuk Maxim on 2018-10-06.
 * Copyright (c) 2018 Fedchuk Maxim All rights reserved.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MapTask app);

    @ApplicationContext
    Context context();

    Application application();

    AppDatabase getAppDatabase();
}
