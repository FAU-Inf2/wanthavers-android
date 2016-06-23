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
import de.fau.cs.mad.wanthavers.common.DesireFilter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 * <p>
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
    public void getDesiresByFilter(@NonNull DesireFilter desireFilter, @NonNull final GetDesiresByFilterCallback callback) {
        checkNotNull(callback);

        /*
        // TODO include to debug filter setting
        DesireFilter abc = desireFilter;
        System.out.println("*****begin print desireFilter in getDesiresByFilter****");
        for (Field field : abc.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String name = field.getName();
            Object value = null;
            try {
                value = field.get(abc);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            System.out.printf("Field name: %s, Field value: %s%n", name, value);
        }
        */

        desireRemoteDataSource.getDesiresByFilter(desireFilter, new GetDesiresByFilterCallback() {
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

    @Override
    public void deleteDesire(@NonNull long desireId, @NonNull final DeleteDesireCallback callback) {
        checkNotNull(desireId);
        checkNotNull(callback);

        desireRemoteDataSource.deleteDesire(desireId, new DeleteDesireCallback() {
            @Override
            public void onDesireDeleted() {
                callback.onDesireDeleted();
            }

            @Override
            public void onDeleteFailed() {
                callback.onDeleteFailed();
            }
        });
    }
}
