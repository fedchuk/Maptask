package com.riffpoint.maptask;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

/**
 * Created by Fedchuk Maxim on 2018-10-06.
 * Copyright (c) 2018 Fedchuk Maxim All rights reserved.
 */
public class AppData {
    private static AppData mInstance;
    private MutableLiveData<Boolean> liveData = new MutableLiveData<>();

    private AppData() {

    }

    public static AppData getInstance() {
        if (mInstance == null) {
            mInstance = new AppData();
        }
        return mInstance;
    }

    public LiveData<Boolean> getLiveData() {
        return liveData;
    }

    public void setLiveData(boolean isConnected) {
        liveData.setValue(isConnected);
    }
}
