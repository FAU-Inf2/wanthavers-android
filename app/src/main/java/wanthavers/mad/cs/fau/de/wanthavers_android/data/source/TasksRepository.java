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
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
public class TasksRepository implements TasksDataSource {

    private static TasksRepository INSTANCE = null;

    private final TasksDataSource mTasksRemoteDataSource;

    private final TasksDataSource mTasksLocalDataSource;

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
    private TasksRepository(@NonNull TasksDataSource tasksRemoteDataSource,
                            @NonNull TasksDataSource tasksLocalDataSource) {
        mTasksRemoteDataSource = checkNotNull(tasksRemoteDataSource);
        mTasksLocalDataSource = checkNotNull(tasksLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param tasksRemoteDataSource the backend data source
     * @param tasksLocalDataSource  the device storage data source
     * @return the {@link TasksRepository} instance
     */
    public static TasksRepository getInstance(TasksDataSource tasksRemoteDataSource,
                                              TasksDataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TasksRepository(tasksRemoteDataSource, tasksLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(TasksDataSource, TasksDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Gets tasks from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     * <p>
     * Note: {@link LoadTasksCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */


    public void acceptDesire(String acceptedDesireId){
        System.out.print("TODO accept desire");
    }

    /*
    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedDesires != null && !mCacheIsDirty) {
            callback.onTasksLoaded(new ArrayList<>(mCachedDesires.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getTasksFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mTasksLocalDataSource.getTasks(new LoadTasksCallback() {
                @Override
                public void onTasksLoaded(List<Desire> desire) {
                    refreshCache(desire);
                    callback.onTasksLoaded(new ArrayList<>(mCachedDesires.values()));
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
        mTasksRemoteDataSource.saveTask(task);
        mTasksLocalDataSource.saveTask(task);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedDesires == null) {
            mCachedDesires = new LinkedHashMap<>();
        }
        mCachedDesires.put(task.getId(), task);
    }

    @Override
    public void clearCompletedTasks() {
        mTasksRemoteDataSource.clearCompletedTasks();
        mTasksLocalDataSource.clearCompletedTasks();

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
     * Note: {@link LoadTasksCallback#onDataNotAvailable()} is fired if both data sources fail to
     * get the data.
     */

    public void getDesire(@NonNull final String desireId, @NonNull final GetTaskCallback callback) {

        checkNotNull(desireId);
        checkNotNull(callback);

        Desire cachedTask = null;  //getDesireWithId(desireId);  TODO just commented this out to test stuff

        // Respond immediately with cache if available
        if (cachedTask != null) {
            callback.onTaskLoaded(cachedTask);
            return;
        }

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.
        mTasksLocalDataSource.getDesire(desireId, new GetTaskCallback() {
            @Override
            public void onTaskLoaded(Desire desire) {
                callback.onTaskLoaded(desire);
            }

            @Override
            public void onDataNotAvailable() {
                mTasksRemoteDataSource.getDesire(desireId, new GetTaskCallback() {
                    @Override
                    public void onTaskLoaded(Desire desire) {
                        callback.onTaskLoaded(desire);
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
        mTasksRemoteDataSource.deleteAllTasks();
        mTasksLocalDataSource.deleteAllTasks();

        if (mCachedDesires == null) {
            mCachedDesires = new LinkedHashMap<>();
        }
        mCachedDesires.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        mTasksRemoteDataSource.deleteTask(checkNotNull(taskId));
        mTasksLocalDataSource.deleteTask(checkNotNull(taskId));

        mCachedDesires.remove(taskId);
    }

    private void getTasksFromRemoteDataSource(@NonNull final LoadTasksCallback callback) {
        mTasksRemoteDataSource.getTasks(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Desire> desires) {
                refreshCache(desires);
                refreshLocalDataSource(desires);
                callback.onTasksLoaded(new ArrayList<>(mCachedDesires.values()));
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
        mTasksLocalDataSource.deleteAllTasks();
        for (Desire desire : desires) {
            mTasksLocalDataSource.saveTask(desire);
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
