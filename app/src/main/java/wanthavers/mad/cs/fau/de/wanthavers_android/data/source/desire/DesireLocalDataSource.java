/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire;

import android.content.Context;
import android.support.annotation.NonNull;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.ormlite.DatabaseHelper;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source as a db.
 */
public class DesireLocalDataSource implements DesireDataSource {

    private static DesireLocalDataSource INSTANCE;

    private RuntimeExceptionDao<Desire, Long> desireDao;

    // Prevent direct instantiation.
    private DesireLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        desireDao = DatabaseHelper.getInstance(context).getDesireRuntimeDao();
    }

    public static DesireLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DesireLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void createDesire(@NonNull Desire desire, @NonNull CreateDesireCallback callback) {
        //TODO: alter this method when we decide to store desires locally
        callback.onCreateFailed();
    }

    @Override
    public void updateDesire(@NonNull Desire desire, @NonNull UpdateDesireCallback callback) {
        //TODO: alter this method when we decide to store desires locally
        callback.onUpdateFailed();
    }

    @Override
    public void updateDesireStatus(@NonNull long desireId, @NonNull int status, @NonNull UpdateDesireStatusCallback callback) {
        //TODO: alter this method when we decide to store desires locally
        callback.onUpdateFailed();
    }

    @Override
    public void getDesire(@NonNull long desireId, @NonNull GetDesireCallback callback) {
        //TODO: alter this method when we decide to store desires locally
        callback.onDataNotAvailable();
    }

    @Override
    public void getDesiresForUser(@NonNull long userId, @NonNull GetDesiresForUserCallback callback) {
        //TODO: alter this method when we decide to store desires locally
        callback.onDataNotAvailable();
    }

    @Override
    public void getAllDesires(@NonNull GetAllDesiresCallback callback) {
        //TODO: alter this method when we decide to store desires locally
        callback.onDataNotAvailable();
    }

    @Override
    public void getDesiresAsHaver(@NonNull long userId, List<Integer> status, @NonNull GetDesiresAsHaverCallback callback) {
        //TODO: alter this method when we decide to store desires locally
        callback.onDataNotAvailable();
    }

    public List<Desire> getAllDesires() {
        return desireDao.queryForAll();
    }

    public void updateDesires(List<Desire> desires) {
        for (Desire d : desires) {
            desireDao.createOrUpdate(d);
        }
    }

    @Override
    public void getDesiresByFilter(Long category, Double price_min, Double price_max, Double reward_min, Float rating_min, Double lat, Double lon, Double radius, List<Integer> status, @NonNull GetDesiresByFilterCallback callback) {
        //TODO: alter this method when we decide to store desires locally
        callback.onDataNotAvailable();
    }
}
