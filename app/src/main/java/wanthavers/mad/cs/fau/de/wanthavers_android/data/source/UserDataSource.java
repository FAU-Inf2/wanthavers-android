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

import de.fau.cs.mad.wanthavers.common.User;

/**
 * Main entry point for accessing user data.
 */
/**
 * Created by Nico on 10.05.2016.
 */
public interface UserDataSource {

    interface GetUserCallback {

        void onUserLoaded(User user);

        void onDataNotAvailable();
    }

    interface GetAllUsers {

        void onAllUsersLoaded(List<User> users);

        void onDataNotAvailable();

    }


    void getUser(@NonNull long userId, @NonNull GetUserCallback callback);

    void getAllUsers(@NonNull GetAllUsers callback);

    void saveUser(@NonNull User user);

    void deleteUser(@NonNull User user);

    void deleteAllUsers();
}
