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

package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.UserDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.local.UserPersistenceContract.UserEntry;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source as a db.
 */
/**
 * Created by Nico on 10.05.2016.
 */
public class UserLocalDataSource implements UserDataSource {

    private static UserLocalDataSource INSTANCE;

    private UserDbHelper mDbHelper;

    private final String[] PROJECTION_ALL_FIELDS = {
            UserEntry.COLUMN_NAME_USER_ID,
            UserEntry.COLUMN_NAME_NAME,
            UserEntry.COLUMN_NAME_EMAIL,
            UserEntry.COLUMN_NAME_BIRTHDAY
    };

    // Prevent direct instantiation.
    private UserLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new UserDbHelper(context);
    }

    public static UserLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new UserLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getUser(@NonNull long userId, @NonNull GetUserCallback callback) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String selection = UserEntry.COLUMN_NAME_USER_ID + " = " + userId;

        Cursor c = db.query(UserEntry.TABLE_NAME, PROJECTION_ALL_FIELDS, selection, null, null, null, null);

        User user = null;

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();

            user = getUserFromCursor(c);
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (user != null) {
            callback.onUserLoaded(user);
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getAllUsers(@NonNull GetAllUsers callback) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        List<User> users = new LinkedList<>();

        Cursor c = db.query(UserEntry.TABLE_NAME, PROJECTION_ALL_FIELDS, null, null, null, null, null);

        if(c.moveToFirst()){
            do {
                User u = getUserFromCursor(c);
                users.add(u);
            } while(c.moveToNext());
        }

        if(c != null) {
            c.close();
        }

        db.close();

        if(users.size() > 0) {
            callback.onAllUsersLoaded(users);
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void saveUser(@NonNull User user) {
        checkNotNull(user);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_NAME_USER_ID, user.getID());
        values.put(UserEntry.COLUMN_NAME_NAME, user.getName());
        values.put(UserEntry.COLUMN_NAME_EMAIL, user.getEmail());
        values.put(UserEntry.COLUMN_NAME_BIRTHDAY, user.getBirthday().getTime());

        db.insert(UserEntry.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void deleteUser(@NonNull User user) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = UserEntry.COLUMN_NAME_USER_ID + " = " + user.getID();

        db.delete(UserEntry.TABLE_NAME, selection, null);

        db.close();
    }

    @Override
    public void deleteAllUsers() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(UserEntry.TABLE_NAME, null, null);

        db.close();
    }

    private User getUserFromCursor(Cursor c){
        User ret = null;

        long userId = c.getLong(c.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_USER_ID));
        String name = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_NAME));
        String email = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_EMAIL));
        long birthday = c.getLong(c.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_BIRTHDAY));

        ret = new User(name, email);
        ret.setId(userId);
        ret.setBirthday(new Date(birthday));

        return ret;
    }
}
