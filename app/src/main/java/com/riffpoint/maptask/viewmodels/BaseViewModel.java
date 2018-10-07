package com.riffpoint.maptask.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.riffpoint.maptask.data.db.AppDatabase;

/**
 * Created by Fedchuk Maxim on 2018-10-06.
 * Copyright (c) 2018 Fedchuk Maxim All rights reserved.
 */
public abstract class BaseViewModel<N> extends AndroidViewModel {

    private final AppDatabase mAppDatabase;
    private N mNavigator;

    public BaseViewModel(AppDatabase appDatabase, Application application) {
        super(application);
        this.mAppDatabase = appDatabase;
    }

    public N getNavigator() {
        return mNavigator;
    }

    public void setNavigator(N navigator) {
        this.mNavigator = navigator;
    }

    public AppDatabase getAppDatabase() {
        return mAppDatabase;
    }
}
