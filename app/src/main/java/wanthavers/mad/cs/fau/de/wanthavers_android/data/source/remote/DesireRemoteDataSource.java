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

package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.remote;

import android.support.annotation.NonNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.DesireDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.DesireClient;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.UserClient;

/**
 * Implementation of the data source that adds a latency simulating network.
 */
public class DesireRemoteDataSource implements DesireDataSource {

    private static DesireRemoteDataSource INSTANCE;

    private DesireClient desireClient = DesireClient.getInstance();
    private UserClient userClient = UserClient.getInstance();

    // Prevent direct instantiation.
    private DesireRemoteDataSource() { }

    public static DesireRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DesireRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void createDesire(@NonNull Desire desire, @NonNull User user, @NonNull CreateDesire callback) {
        try {
            Desire ret = desireClient.createDesire(desire, user);
            callback.onDesireCreated(ret);
        } catch (Throwable t) {
            callback.onCreateFailed();
        }
    }

    @Override
    public void updateDesire(@NonNull Desire desire, @NonNull UpdateDesire callback) {
        try {
            Desire ret = desireClient.updateDesire(desire.getID(), desire);
            callback.onDesireUpdated(ret);
        } catch (Throwable t) {
            callback.onUpdateFailed();
        }
    }

    @Override
    public void deleteDesire(@NonNull Desire desire, @NonNull DeleteDesire callback) {
        deleteDesire(desire.getID(), callback);
    }

    @Override
    public void deleteDesire(@NonNull long desireId, @NonNull DeleteDesire callback) {
        try {
            desireClient.deleteDesire(desireId);
            callback.onDesireDeleted();
        } catch (Throwable t) {
            callback.onDeleteFailed();
        }
    }

    @Override
    public void getDesire(@NonNull long desireId, final @NonNull GetDesireCallback callback) {
        try {
            final Desire desire = desireClient.get(desireId);
            callback.onDesireLoaded(desire);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getDesiresForUser(@NonNull long userId, @NonNull GetDesiresForUser callback) {
        try {
            final List<Desire> desiresForUser = userClient.getDesires(userId);
            callback.onDesiresForUserLoaded(desiresForUser);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getAllDesires(@NonNull GetAllDesires callback) {
        try {
            final List<Desire> allDesires = desireClient.get();
            callback.onAllDesiresLoaded(allDesires);
        } catch(Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getDesireByLocation(@NonNull double lat, @NonNull double lon, @NonNull double radius, @NonNull GetDesiresByLocation callback) {
        try {
            final List<Desire> desiresByLocation = desireClient.getByLocation(lat, lon, radius);
            callback.onDesiresByLocationLoaded(desiresByLocation);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }
}
