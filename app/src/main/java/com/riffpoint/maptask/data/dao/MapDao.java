package com.riffpoint.maptask.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.riffpoint.maptask.data.entity.MapEntity;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Fedchuk Maxim on 2018-10-06.
 * Copyright (c) 2018 Fedchuk Maxim All rights reserved.
 */
@Dao
public interface MapDao {
    @Insert
    long[] addAllMap(List<MapEntity> mapEntities);

    @Query("SELECT * FROM maptask_map")
    Flowable<List<MapEntity>> getAllMapItems();
}
