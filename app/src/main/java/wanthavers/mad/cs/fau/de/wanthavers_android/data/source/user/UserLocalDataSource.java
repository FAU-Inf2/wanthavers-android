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

package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.Date;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.ormlite.DatabaseHelper;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source as a db.
 */
/**
 * Created by Nico on 10.05.2016.
 */
public class UserLocalDataSource implements UserDataSource {

    private static UserLocalDataSource INSTANCE;

    private RuntimeExceptionDao<User, Long> userDao;

    // Prevent direct instantiation.
    private UserLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        userDao = DatabaseHelper.getInstance(context).getUserRuntimeDao();
    }

    public static UserLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new UserLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getUser(@NonNull long userId, @NonNull GetUserCallback callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getAllUsers(@NonNull GetAllUsersCallback callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void createUser(@NonNull User user, @NonNull String password, @NonNull CreateUserCallback callback) {
        callback.onCreationFailed();
    }

    @Override
    public void updateUser(@NonNull User user, @NonNull UpdateUserCallback callback) {
        callback.onUpdateFailed();
    }

    @Override
    public void deleteUser(@NonNull DeleteUserCallback callback) {
        callback.onDeleteFailed();
    }

    @Override
    public void login(@NonNull LoginCallback callback) {
        callback.onLoginFailed();
    }
}
