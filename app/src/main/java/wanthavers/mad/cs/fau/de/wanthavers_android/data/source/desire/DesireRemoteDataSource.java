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

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.DesireFilter;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.DesireClient;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.UserClient;

/**
 * Implementation of the data source that adds a latency simulating network.
 */
public class DesireRemoteDataSource implements DesireDataSource {

    private static DesireRemoteDataSource INSTANCE;

    private DesireClient desireClient;
    private UserClient userClient;

    // Prevent direct instantiation.
    private DesireRemoteDataSource(Context context) {
        desireClient = DesireClient.getInstance(context);
        userClient = UserClient.getInstance(context);
    }

    public static DesireRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DesireRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void createDesire(@NonNull Desire desire, @NonNull CreateDesireCallback callback) {
        try {
            Desire ret = desireClient.createDesire(desire);
            callback.onDesireCreated(ret);
        } catch (Throwable t) {
            callback.onCreateFailed();
        }
    }

    @Override
    public void updateDesire(@NonNull Desire desire, @NonNull UpdateDesireCallback callback) {
        try {
            Desire ret = desireClient.updateDesire(desire.getId(), desire);
            callback.onDesireUpdated(ret);
        } catch (Throwable t) {
            callback.onUpdateFailed();
        }
    }

    @Override
    public void updateDesireStatus(@NonNull long desireId, @NonNull int status, @NonNull UpdateDesireStatusCallback callback) {
        try {
            Desire ret = desireClient.updateDesireStatus(desireId, status);
            callback.onStatusUpdated(ret);
        } catch (Throwable t) {
            System.out.println("UpdateDesireStatus-Error: " + t);
            callback.onUpdateFailed();
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
    public void getDesiresForUser(@NonNull long userId, @NonNull GetDesiresForUserCallback callback) {
        try {
            final List<Desire> desiresForUser = userClient.getDesires(userId);
            callback.onDesiresForUserLoaded(desiresForUser);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getDesiresAsHaver(@NonNull long userId, List<Integer> status, @NonNull GetDesiresAsHaverCallback callback) {
        try {
            List<Desire> desires = userClient.getDesiresAsHaver(userId, status);
            callback.onDesiresAsHaverLoaded(desires);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getDesiresByFilter(@NonNull DesireFilter desireFilter, @NonNull GetDesiresByFilterCallback callback) {
        try {
            List<Desire> ret = desireClient.getByFilter(desireFilter.getCategory(),
                                                        desireFilter.getPrice_min(),
                                                        desireFilter.getPrice_max(),
                                                        desireFilter.getReward_min(),
                                                        desireFilter.getRating_min(),
                                                        desireFilter.getLat(),
                                                        desireFilter.getLon(),
                                                        desireFilter.getRadius(),
                                                        desireFilter.getStatus(),
                                                        desireFilter.getLastDesireId(),
                                                        desireFilter.getLimit(),
                                                        desireFilter.getCreatorId(),
                                                        desireFilter.getHaverId(),
                                                        desireFilter.getHaverStatus());
            callback.onDesiresByFilterLoaded(ret);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getChatForDesire(@NonNull long user2Id, @NonNull long desireId, @NonNull GetChatForDesireCallback callback) {
        try {
            Chat ret = desireClient.getChatForDesire(user2Id, desireId);
            callback.onChatLoaded(ret);
        } catch (Throwable t) {
            callback.onLoadFailed();
        }
    }
}
