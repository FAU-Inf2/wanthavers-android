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

package wanthavers.mad.cs.fau.de.wanthavers_android.data.source;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

//import wanthavers.mad.cs.fau.de.wanthavers_android.data.Desire;
import de.fau.cs.mad.wanthavers.common.Desire;
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

    private final DesireDataSource mDesireRemoteDataSource;

    private final DesireDataSource mDesireLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Desire> mCachedDesires;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private DesireRepository(@NonNull DesireDataSource desireRemoteDataSource,
                             @NonNull DesireDataSource desireLocalDataSource) {
        mDesireRemoteDataSource = checkNotNull(desireRemoteDataSource);
        mDesireLocalDataSource = checkNotNull(desireLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param desireRemoteDataSource the backend data source
     * @param desireLocalDataSource  the device storage data source
     * @return the {@link DesireRepository} instance
     */
    public static DesireRepository getInstance(DesireDataSource desireRemoteDataSource,
                                               DesireDataSource desireLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new DesireRepository(desireRemoteDataSource, desireLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(DesireDataSource, DesireDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Gets tasks from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     * <p>
     * Note: {@link LoadDesireCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */


    public void acceptDesire(long acceptedDesireId){
        System.out.print("TODO accept desire");
    }

    /*
    @Override
    public void getTasks(@NonNull final LoadDesireCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedDesires != null && !mCacheIsDirty) {
            callback.onDesireLoaded(new ArrayList<>(mCachedDesires.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getTasksFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mDesireLocalDataSource.getTasks(new LoadDesireCallback() {
                @Override
                public void onDesireLoaded(List<Desire> desire) {
                    refreshCache(desire);
                    callback.onDesireLoaded(new ArrayList<>(mCachedDesires.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getTasksFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void saveTask(@NonNull Desire task) {
        checkNotNull(task);
        mDesireRemoteDataSource.saveTask(task);
        mDesireLocalDataSource.saveTask(task);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedDesires == null) {
            mCachedDesires = new LinkedHashMap<>();
        }
        mCachedDesires.put(task.getId(), task);
    }

    @Override
    public void clearCompletedTasks() {
        mDesireRemoteDataSource.clearCompletedTasks();
        mDesireLocalDataSource.clearCompletedTasks();

        // Do in memory cache update to keep the app UI up to date
        if (mCachedDesires == null) {
            mCachedDesires = new LinkedHashMap<>();
        }
        Iterator<Map.Entry<String, Desire>> it = mCachedDesires.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Desire> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }
    }

    /**
     * Gets tasks from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     * <p>
     * Note: {@link LoadDesireCallback#onDataNotAvailable()} is fired if both data sources fail to
     * get the data.
     */

    public void getDesire(@NonNull final long desireId, @NonNull final GetDesireCallback callback) {

        checkNotNull(desireId);
        checkNotNull(callback);

        //TODO just commented this out to test stuff
        //I don't think that we need this because as soon as the local data source has
        //cached something it will return the callback method for on...Loaded

/*        Desire cachedTask = null;  //getDesireWithId(desireId);

        // Respond immediately with cache if available
        if (cachedTask != null) {
            callback.onDesireLoaded(cachedTask);
            return;
        }*/

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.
        mDesireLocalDataSource.getDesire(desireId, new GetDesireCallback() {
            @Override
            public void onDesireLoaded(Desire desire) {
                callback.onDesireLoaded(desire);
            }

            @Override
            public void onDataNotAvailable() {
                mDesireRemoteDataSource.getDesire(desireId, new GetDesireCallback() {
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
    public void getDesiresForUser(@NonNull final long userId, @NonNull final GetDesiresForUser callback) {
        checkNotNull(userId);
        checkNotNull(callback);

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.
        mDesireLocalDataSource.getDesiresForUser(userId, new GetDesiresForUser() {
            @Override
            public void onDesiresForUserLoaded(List<Desire> desires) {
                callback.onDesiresForUserLoaded(desires);
            }

            @Override
            public void onDataNotAvailable() {
                mDesireRemoteDataSource.getDesiresForUser(userId, new GetDesiresForUser() {
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
    public void getAllDesires(@NonNull final GetAllDesires callback) {
        checkNotNull(callback);

        mDesireLocalDataSource.getAllDesires(new GetAllDesires() {
            @Override
            public void onAllDesiresLoaded(List<Desire> desires) {
                callback.onAllDesiresLoaded(desires);
            }

            @Override
            public void onDataNotAvailable() {
                mDesireRemoteDataSource.getAllDesires(new GetAllDesires() {
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

    /*
    @Override
    public void refreshTasks() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllTasks() {
        mDesireRemoteDataSource.deleteAllTasks();
        mDesireLocalDataSource.deleteAllTasks();

        if (mCachedDesires == null) {
            mCachedDesires = new LinkedHashMap<>();
        }
        mCachedDesires.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        mDesireRemoteDataSource.deleteTask(checkNotNull(taskId));
        mDesireLocalDataSource.deleteTask(checkNotNull(taskId));

        mCachedDesires.remove(taskId);
    }

    private void getTasksFromRemoteDataSource(@NonNull final LoadDesireCallback callback) {
        mDesireRemoteDataSource.getTasks(new LoadDesireCallback() {
            @Override
            public void onDesireLoaded(List<Desire> desires) {
                refreshCache(desires);
                refreshLocalDataSource(desires);
                callback.onDesireLoaded(new ArrayList<>(mCachedDesires.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Desire> desires) {
        if (mCachedDesires == null) {
            mCachedDesires = new LinkedHashMap<>();
        }
        mCachedDesires.clear();
        for (Desire desire : desires) {
            mCachedDesires.put(desire.getId(), desire);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Desire> desires) {
        mDesireLocalDataSource.deleteAllTasks();
        for (Desire desire : desires) {
            mDesireLocalDataSource.saveTask(desire);
        }
    }

    @Nullable
    private Desire getDesireWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedDesires == null || mCachedDesires.isEmpty()) {
            return null;
        } else {
            return mCachedDesires.get(id);
        }
    }
    */
}
