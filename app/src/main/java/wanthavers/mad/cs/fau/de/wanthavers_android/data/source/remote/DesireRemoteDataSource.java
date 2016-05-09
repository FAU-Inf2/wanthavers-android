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

import android.os.Handler;
import android.support.annotation.NonNull;


import java.util.LinkedHashMap;
import java.util.Map;
import de.fau.cs.mad.wanthavers.common.Desire;
//import wanthavers.mad.cs.fau.de.wanthavers_android.data.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.DesireDataSource;

/**
 * Implementation of the data source that adds a latency simulating network.
 */
public class DesireRemoteDataSource implements DesireDataSource {

    private static DesireRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private static final Map<String, Desire> TASKS_SERVICE_DATA;

    static {
        TASKS_SERVICE_DATA = new LinkedHashMap<>(2);
        //addTask("Build tower in Pisa", "Ground looks good, no foundation work required.");
        //addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!");
    }

    public static DesireRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DesireRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private DesireRemoteDataSource() {}

    private static void addTask(String id, String title, String description) {
        Desire newTask = new Desire("TestLocalDbLayer", "TestLocalDbLayerDesc",null,0,0,null,null,0,0);

        String taskId = Long.toString(newTask.getID());
        TASKS_SERVICE_DATA.put(id, newTask);
    }

    /**
     * Note: {@link LoadDesireCallback#onDataNotAvailable()} is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    /*
    @Override

    public void getTasks(final @NonNull LoadDesireCallback callback) {
        // Simulate network by delaying the execution.
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onDesireLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values()));
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }
    */
    /**
     * Note: {@link GetDesireCallback#onDataNotAvailable()} is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    @Override
    public void getDesire(@NonNull long desireId, final @NonNull GetDesireCallback callback) {
        final Desire desire = TASKS_SERVICE_DATA.get(desireId);

        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onTaskLoaded(desire);
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    /*
    @Override
    public void saveTask(Desire desire) {
        TASKS_SERVICE_DATA.put(desire.getId(), desire);
    }


    @Override
    public void completeTask(Desire desire) {
        Desire completedTask = new Desire(desire.getId(), desire.getTitle(), desire.getDescription());
        TASKS_SERVICE_DATA.put(desire.getId(), completedTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        // Not required for the remote data source because the {@link DesireRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    @Override
    public void activateTask(Desire desire) {
        Desire activeTask = new Desire(desire.getTitle(), desire.getDescription(), desire.getId());
        TASKS_SERVICE_DATA.put(desire.getId(), activeTask);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        // Not required for the remote data source because the {@link DesireRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    @Override
    public void clearCompletedTasks() {
        Iterator<Map.Entry<String, Desire>> it = TASKS_SERVICE_DATA.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Desire> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }
    }

    @Override
    public void refreshTasks() {
        // Not required because the {@link DesireRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }


    @Override
    public void deleteAllTasks() {
        TASKS_SERVICE_DATA.clear();
    }

    @Override
    public void deleteTask(String taskId) {
        TASKS_SERVICE_DATA.remove(taskId);
    }
    */
}
