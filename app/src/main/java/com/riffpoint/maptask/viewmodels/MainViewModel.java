package com.riffpoint.maptask.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.riffpoint.maptask.data.db.AppDatabase;
import com.riffpoint.maptask.data.entity.MapEntity;
import com.riffpoint.maptask.data.model.Items;
import com.riffpoint.maptask.data.model.Location;
import com.riffpoint.maptask.navigators.MainNavigator;
import com.riffpoint.maptask.utils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Fedchuk Maxim on 2018-10-06.
 * Copyright (c) 2018 Fedchuk Maxim All rights reserved.
 */
public class MainViewModel extends BaseViewModel<MainNavigator> {

    private Context mContext;
    public ObservableList<Items> itemsList = new ObservableArrayList<>();
    public ObservableField<Double> latitude = new ObservableField<>(0.0);
    public ObservableField<Double> longitude = new ObservableField<>(0.0);

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            latitude.set(location.getLatitude());
            longitude.set(location.getLongitude());
            getNavigator().setCurrentPosition(latitude.get(), longitude.get());
            getNavigator().hideLoading();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public MainViewModel(AppDatabase appDatabase, Application application) {
        super(appDatabase, application);
        mContext = getApplication().getApplicationContext();
    }

    @SuppressLint("CheckResult")
    public void getDataFromBD() {
        getAppDatabase().getMapDao().getAllMapItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MapEntity>>() {
                    @Override
                    public void accept(@NonNull List<MapEntity> mapEntities) throws Exception {
                        if (!mapEntities.isEmpty()) {
                            for (int i=0; i<mapEntities.size(); i++) {
                                MapEntity me = mapEntities.get(i);
                                itemsList.add(new Items(me.getId(), me.getName(), new Location(Double.valueOf(me.getLatitude()),
                                        Double.valueOf(me.getLongitude()))));
                            }
                            getNavigator().setDataItems(itemsList);
                        } else {
                            getData();
                        }
                    }
                });
    }

    private void getData() {
        String json = CommonUtils.loadJSONFromAsset(mContext);
        if(!TextUtils.isEmpty(json)) {
            List<MapEntity> mapEntityList = new ArrayList<>();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                JSONArray items = jsonObject.getJSONArray("items");

                for (int i=0; i < items.length(); i++){
                    JSONObject jo = items.getJSONObject(i);
                    JSONObject joLoc = jo.getJSONObject("location");
                    mapEntityList.add(new MapEntity(jo.getInt("id"), jo.getString("name"),
                            String.valueOf(joLoc.getDouble("latitude")), String.valueOf(joLoc.getDouble("longitude"))));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(!mapEntityList.isEmpty()) {
                    getAppDatabase().getMapDao().addAllMap(mapEntityList);
                }
            }
        }
    }

    public String getTitleFromLatLng(LatLng latLng) {
        if(itemsList.isEmpty()) {
            return "";
        }
        for (Items items : itemsList) {
            if(items.getLocation().getLatitude() == latLng.latitude&&
                    items.getLocation().getLongitude() == latLng.longitude) {
                return items.getName();
            }
        }
        return "";
    }
}
