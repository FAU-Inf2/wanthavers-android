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

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Desire;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 * <p/>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
public class DesireRepository implements DesireDataSource {

    private static DesireRepository INSTANCE = null;

    private final DesireRemoteDataSource desireRemoteDataSource;

    private final DesireLocalDataSource desireLocalDataSource;

    // Prevent direct instantiation.
    private DesireRepository(@NonNull DesireRemoteDataSource desireRemoteDataSource,
                             @NonNull DesireLocalDataSource desireLocalDataSource) {
        this.desireRemoteDataSource = checkNotNull(desireRemoteDataSource);
        this.desireLocalDataSource = checkNotNull(desireLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param desireRemoteDataSource the backend data source
     * @param desireLocalDataSource  the device storage data source
     * @return the {@link DesireRepository} instance
     */
    public static DesireRepository getInstance(DesireRemoteDataSource desireRemoteDataSource,
                                               DesireLocalDataSource desireLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new DesireRepository(desireRemoteDataSource, desireLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(DesireRemoteDataSource, DesireLocalDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void createDesire(@NonNull final Desire desire, @NonNull final CreateDesireCallback callback) {
        checkNotNull(desire);
        checkNotNull(callback);

        desireRemoteDataSource.createDesire(desire, new CreateDesireCallback() {
            @Override
            public void onDesireCreated(Desire desire) {
                callback.onDesireCreated(desire);
            }

            @Override
            public void onCreateFailed() {
                callback.onCreateFailed();
            }
        });
    }

    @Override
    public void updateDesire(@NonNull final Desire desire, @NonNull final UpdateDesireCallback callback) {
        checkNotNull(desire);
        checkNotNull(callback);

        desireRemoteDataSource.updateDesire(desire, new UpdateDesireCallback() {
            @Override
            public void onDesireUpdated(Desire desire) {
                callback.onDesireUpdated(desire);
            }

            @Override
            public void onUpdateFailed() {
                callback.onUpdateFailed();
            }
        });
    }

    @Override
    public void updateDesireStatus(@NonNull long desireId, @NonNull int status, @NonNull final UpdateDesireStatusCallback callback) {
        checkNotNull(desireId);
        checkNotNull(status);
        checkNotNull(callback);

        desireRemoteDataSource.updateDesireStatus(desireId, status, new UpdateDesireStatusCallback() {
            @Override
            public void onStatusUpdated(Desire desire) {
                callback.onStatusUpdated(desire);
            }

            @Override
            public void onUpdateFailed() {
                callback.onUpdateFailed();
            }
        });
    }

    public void getDesire(@NonNull final long desireId, @NonNull final GetDesireCallback callback) {
        checkNotNull(desireId);
        checkNotNull(callback);

        desireLocalDataSource.getDesire(desireId, new GetDesireCallback() {
            @Override
            public void onDesireLoaded(Desire desire) {
                callback.onDesireLoaded(desire);
            }

            @Override
            public void onDataNotAvailable() {
                desireRemoteDataSource.getDesire(desireId, new GetDesireCallback() {
                    @Override
                    public void onDesireLoaded(Desire desire) {
                        callback.onDesireLoaded(desire);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });

    }

    @Override
    public void getDesiresForUser(@NonNull final long userId, @NonNull final GetDesiresForUserCallback callback) {
        checkNotNull(userId);
        checkNotNull(callback);

        desireLocalDataSource.getDesiresForUser(userId, new GetDesiresForUserCallback() {
            @Override
            public void onDesiresForUserLoaded(List<Desire> desires) {
                callback.onDesiresForUserLoaded(desires);
            }

            @Override
            public void onDataNotAvailable() {
                desireRemoteDataSource.getDesiresForUser(userId, new GetDesiresForUserCallback() {
                    @Override
                    public void onDesiresForUserLoaded(List<Desire> desires) {
                        callback.onDesiresForUserLoaded(desires);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void getAllDesires(@NonNull final GetAllDesiresCallback callback) {
        checkNotNull(callback);

/*        List<Desire> desiresRemote = desireRemoteDataSource.getAllDesires();

        if (!desiresRemote.isEmpty()) {
            desireLocalDataSource.updateDesires(desiresRemote);
        }

        List<Desire> desiresLocal = desireLocalDataSource.getAllDesires();

        if (!desiresLocal.isEmpty()) {
            callback.onAllDesiresLoaded(desiresLocal);
        } else {
            callback.onDataNotAvailable();
        }*/

        desireLocalDataSource.getAllDesires(new GetAllDesiresCallback() {
            @Override
            public void onAllDesiresLoaded(List<Desire> desires) {
                callback.onAllDesiresLoaded(desires);
            }

            @Override
            public void onDataNotAvailable() {
                desireRemoteDataSource.getAllDesires(new GetAllDesiresCallback() {
                    @Override
                    public void onAllDesiresLoaded(List<Desire> desires) {
                        callback.onAllDesiresLoaded(desires);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void getDesiresAsHaver(@NonNull final long userId, final List<Integer> status, @NonNull final GetDesiresAsHaverCallback callback) {
        checkNotNull(userId);
        checkNotNull(callback);

        desireLocalDataSource.getDesiresAsHaver(userId, status, new GetDesiresAsHaverCallback() {
            @Override
            public void onDesiresAsHaverLoaded(List<Desire> desires) {
                callback.onDesiresAsHaverLoaded(desires);
            }

            @Override
            public void onDataNotAvailable() {
                desireRemoteDataSource.getDesiresAsHaver(userId, status, new GetDesiresAsHaverCallback() {
                    @Override
                    public void onDesiresAsHaverLoaded(List<Desire> desires) {
                        callback.onDesiresAsHaverLoaded(desires);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void getDesiresByFilter(Long category, Double price_min, Double price_max, Double reward_min, Float rating_min, Double lat, Double lon, Double radius, List<Integer> status, Long lastCreationTime, Integer limit, @NonNull final GetDesiresByFilterCallback callback) {
        checkNotNull(callback);

        desireRemoteDataSource.getDesiresByFilter(category, price_min, price_max, reward_min, rating_min, lat, lon, radius, status, lastCreationTime, limit, new GetDesiresByFilterCallback() {
            @Override
            public void onDesiresByFilterLoaded(List<Desire> desires) {
                callback.onDesiresByFilterLoaded(desires);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getChatForDesire(@NonNull long user2Id, @NonNull long desireId, @NonNull final GetChatForDesireCallback callback) {
        checkNotNull(user2Id);
        checkNotNull(desireId);
        checkNotNull(callback);

        desireRemoteDataSource.getChatForDesire(user2Id, desireId, new GetChatForDesireCallback() {
            @Override
            public void onChatLoaded(Chat chat) {
                callback.onChatLoaded(chat);
            }

            @Override
            public void onLoadFailed() {
                callback.onLoadFailed();
            }
        });
    }
}
