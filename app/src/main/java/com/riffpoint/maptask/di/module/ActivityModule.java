package com.riffpoint.maptask.di.module;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.riffpoint.maptask.data.db.AppDatabase;
import com.riffpoint.maptask.di.ActivityContext;
import com.riffpoint.maptask.di.PerActivity;
import com.riffpoint.maptask.viewmodels.MainViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Fedchuk Maxim on 2018-10-06.
 * Copyright (c) 2018 Fedchuk Maxim All rights reserved.
 */
@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    @PerActivity
    MainViewModel provideMainViewModel(AppDatabase appDatabase, Application application) {
        return new MainViewModel(appDatabase, application);
    }
}
