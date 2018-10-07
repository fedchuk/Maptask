package com.riffpoint.maptask.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import com.riffpoint.maptask.AppData;

/**
 * Created by Fedchuk Maxim on 2018-10-07.
 * Copyright (c) 2018 Fedchuk Maxim All rights reserved.
 */
public class GpsChangingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AppData.getInstance().setLiveData(isGpsOn(context));
    }

    private boolean isGpsOn(Context context) {
        try {
            LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                return true;
            } else {
                return false;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
