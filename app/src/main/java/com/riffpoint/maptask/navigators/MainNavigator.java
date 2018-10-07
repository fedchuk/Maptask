package com.riffpoint.maptask.navigators;

import com.riffpoint.maptask.data.model.Items;

import java.util.List;

/**
 * Created by Fedchuk Maxim on 2018-10-06.
 * Copyright (c) 2018 Fedchuk Maxim All rights reserved.
 */
public interface MainNavigator {
    void hideLoading();
    void setDataItems(List<Items> itemsList);
    void setCurrentPosition(double latitude, double longitude);
}
