package com.riffpoint.maptask;

import android.app.Application;
import android.content.Context;

import com.riffpoint.maptask.di.components.ApplicationComponent;
import com.riffpoint.maptask.di.components.DaggerApplicationComponent;
import com.riffpoint.maptask.di.module.ApplicationModule;


/**
 * Created by Fedchuk Maxim on 2018-10-06.
 * Copyright (c) 2018 Fedchuk Maxim All rights reserved.
 */
public class MapTask extends Application {
    private static ApplicationComponent mApplicationComponent;

    public static Context getContext() {
        return mApplicationComponent.context();
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);
    }
}
