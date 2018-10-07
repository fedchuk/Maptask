package com.riffpoint.maptask.activities;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.riffpoint.maptask.AppData;
import com.riffpoint.maptask.BR;
import com.riffpoint.maptask.R;
import com.riffpoint.maptask.data.model.Items;
import com.riffpoint.maptask.databinding.ActivityMainBinding;
import com.riffpoint.maptask.databinding.InfoWindowBinding;
import com.riffpoint.maptask.navigators.MainNavigator;
import com.riffpoint.maptask.receivers.GpsChangingReceiver;
import com.riffpoint.maptask.utils.AppConstants;
import com.riffpoint.maptask.utils.CommonUtils;
import com.riffpoint.maptask.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Fedchuk Maxim on 2018-10-06.
 * Copyright (c) 2018 Fedchuk Maxim All rights reserved.
 */
public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements MainNavigator, OnMapReadyCallback {

    @Inject
    MainViewModel mMainViewModel;
    ActivityMainBinding mActivityMainBinding;

    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 97;
    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;
    private GpsChangingReceiver mGpsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = getViewDataBinding();
        mMainViewModel.setNavigator(this);
        init();
        LiveData<Boolean> liveData = AppData.getInstance().getLiveData();
        liveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isConnected) {
                if (isConnected){
                    startUpdate();
                }
            }
        });
        getViewModel().getDataFromBD();
        registerGpsBroadcast();
        getCurrentLocation();
    }

    @Override
    public MainViewModel getViewModel() {
        return mMainViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void performDependencyInjection() {
        getActivityComponent().inject(this);
    }

    private void init() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                InfoWindowBinding infoWindowBinding = DataBindingUtil.inflate((MainActivity.this).getLayoutInflater(),
                        R.layout.info_window, null, false);
                LatLng latLng = marker.getPosition();
                String title = getViewModel().getTitleFromLatLng(latLng);
                if(!TextUtils.isEmpty(title)) {
                    infoWindowBinding.textTitle.setText(title);
                }
                infoWindowBinding.rating.setRating(3.0f);
                infoWindowBinding.textOwner.setText(CommonUtils.underlineAllString(infoWindowBinding.textOwner.getText().toString()));
                return infoWindowBinding.getRoot();
            }
        });
    }

    public void showSettingAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getResources().getString(R.string.activity_main_enable_location))
                .setMessage(getResources().getString(R.string.activity_main_mess_location))
                .setPositiveButton(getResources().getString(R.string.activity_main_setting_location),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.activity_main_cancel_location), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showSettingAlert();
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    // permission denied
                }
                break;
            }

        }
    }

    public void getCurrentLocation() {
        showLoading();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!checkLocation())
            return;

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, getViewModel().locationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, getViewModel().locationListener);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);
        }
        startUpdate();
    }

    public void startUpdate() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            showLoading();
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, AppConstants.minTimeLocation,
                    AppConstants.minDistanceLocation, getViewModel().locationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, AppConstants.minTimeLocation,
                    AppConstants.minDistanceLocation, getViewModel().locationListener);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterGpsChanges();
    }

    private void registerGpsBroadcast() {
        mGpsReceiver = new GpsChangingReceiver();
        registerReceiver(mGpsReceiver, new IntentFilter(LocationManager.MODE_CHANGED_ACTION));
    }

    public void unregisterGpsChanges() {
        try {
            unregisterReceiver(mGpsReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDataItems(List<Items> itemsList) {
        if (!itemsList.isEmpty()) {
            List<LatLng> latLngs = new ArrayList<>();
            for (int i = 0; i < itemsList.size(); i++) {
                Items items = itemsList.get(i);
                LatLng latLng = new LatLng(items.getLocation().getLatitude(), items.getLocation().getLongitude());
                latLngs.add(latLng);
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(items.getName())
                        .icon(CommonUtils.bitmapMapFromVector(this, R.drawable.ic_gps))
                        .snippet(items.getName()));
            }
            setPosition(latLngs);
        }
    }

    @Override
    public void setCurrentPosition(double latitude, double longitude) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
    }

    private void setPosition(List<LatLng> latLngs){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngs) {
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        int padding = 0;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mGoogleMap.moveCamera(cu);
        mGoogleMap.animateCamera(cu);
    }
}
