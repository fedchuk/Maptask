package com.riffpoint.maptask.di.components;

import com.riffpoint.maptask.activities.MainActivity;
import com.riffpoint.maptask.di.PerActivity;
import com.riffpoint.maptask.di.module.ActivityModule;

import dagger.Component;

/**
 * Created by Fedchuk Maxim on 2018-10-06.
 * Copyright (c) 2018 Fedchuk Maxim All rights reserved.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity activity);
}
