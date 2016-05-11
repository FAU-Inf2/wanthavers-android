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
import de.fau.cs.mad.wanthavers.common.Desire;

/**
 * Main entry point for accessing tasks data.
 */
public interface DesireDataSource {

    interface LoadDesireCallback {

        void onDesireLoaded(List<Desire> desireList);

        void onDataNotAvailable();
    }

    interface GetDesireCallback {

        void onDesireLoaded(Desire desire);

        void onDataNotAvailable();
    }

    interface GetDesiresForUser {

        void onDesiresForUserLoaded(List<Desire> desires);

        void onDataNotAvailable();

    }

    interface GetAllDesires {

        void onAllDesiresLoaded(List<Desire> desires);

        void onDataNotAvailable();

    }


    void getDesire(@NonNull long desireId, @NonNull GetDesireCallback callback);

    void getDesiresForUser(@NonNull long userId, @NonNull GetDesiresForUser callback);

    void getAllDesires(@NonNull GetAllDesires callback);
}
