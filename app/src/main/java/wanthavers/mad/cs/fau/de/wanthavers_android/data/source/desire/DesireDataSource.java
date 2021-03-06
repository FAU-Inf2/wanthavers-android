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

/**
 * Main entry point for accessing tasks data.
 */
public interface DesireDataSource {

    interface GetDesireCallback {

        void onDesireLoaded(Desire desire);

        void onDataNotAvailable();
    }

    interface GetDesiresByFilterCallback {

        void onDesiresByFilterLoaded(List<Desire> desires);

        void onDataNotAvailable();

    }

    interface CreateDesireCallback {

        void onDesireCreated(Desire desire);

        void onCreateFailed();

    }

    interface UpdateDesireCallback {

        void onDesireUpdated(Desire desire);

        void onUpdateFailed();

    }

    interface UpdateDesireStatusCallback {

        void onStatusUpdated(Desire desire);

        void onUpdateFailed();

    }

    interface GetChatForDesireCallback {

        void onChatLoaded(Chat chat);

        void onLoadFailed();

    }

    interface DeleteDesireCallback {

        void onDesireDeleted();

        void onDeleteFailed();

    }

    void createDesire(@NonNull Desire desire, @NonNull CreateDesireCallback callback);

    void updateDesire(@NonNull Desire desire, @NonNull UpdateDesireCallback callback);

    void updateDesireStatus(@NonNull long desireId, @NonNull int status, @NonNull UpdateDesireStatusCallback callback);

    void getDesire(@NonNull long desireId, @NonNull GetDesireCallback callback);

    void getDesiresByFilter(@NonNull DesireFilter desireFilter, @NonNull GetDesiresByFilterCallback callback);

    void getChatForDesire(@NonNull long user2Id, @NonNull long desireId, @NonNull GetChatForDesireCallback callback);

    void deleteDesire(@NonNull long desireId, @NonNull DeleteDesireCallback callback);
}
