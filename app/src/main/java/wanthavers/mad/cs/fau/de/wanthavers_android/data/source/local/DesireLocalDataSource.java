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

package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.DesireDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source as a db.
 */
public class DesireLocalDataSource implements DesireDataSource {

    private static DesireLocalDataSource INSTANCE;

    private DesireDbHelper mDbHelper;

    // Prevent direct instantiation.
    private DesireLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new DesireDbHelper(context);
    }

    public static DesireLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DesireLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void createDesire(@NonNull Desire desire, @NonNull User user, @NonNull CreateDesire callback) {
        //TODO: alter this method when we decide to store desires locally
        callback.onCreateFailed();
    }

    @Override
    public void updateDesire(@NonNull Desire desire, @NonNull UpdateDesire callback) {
        //TODO: alter this method when we decide to store desires locally
        callback.onUpdateFailed();
    }

    @Override
    public void deleteDesire(@NonNull Desire desire, @NonNull DeleteDesire callback) {
        //TODO: alter this method when we decide to store desires locally
        callback.onDeleteFailed();
    }

    @Override
    public void deleteDesire(@NonNull long desireId, @NonNull DeleteDesire callback) {
        //TODO: alter this method when we decide to store desires locally
        callback.onDeleteFailed();
    }

    @Override
    public void getDesire(@NonNull long desireId, @NonNull GetDesireCallback callback) {
        //TODO: alter this method when we decide to store desires locally
        callback.onDataNotAvailable();
    }

    @Override
    public void getDesiresForUser(@NonNull long userId, @NonNull GetDesiresForUser callback) {
        //TODO: alter this method when we decide to store desires locally
        callback.onDataNotAvailable();
    }

    @Override
    public void getAllDesires(@NonNull GetAllDesires callback) {
        //TODO: alter this method when we decide to store desires locally
        callback.onDataNotAvailable();
    }

    @Override
    public void getDesireByLocation(@NonNull double lat, @NonNull double lon, @NonNull double radius, @NonNull GetDesiresByLocation callback) {
        //TODO: alter this method when we decide to store desires locally
        callback.onDataNotAvailable();
    }
}
