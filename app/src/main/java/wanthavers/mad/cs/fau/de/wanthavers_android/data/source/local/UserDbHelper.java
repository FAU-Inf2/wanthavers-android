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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "WantHaver.db";

    //SQLite Integer can save up to 8 Byte
    private static final String LONG_TYPE = " INTEGER";

    private static final String TEXT_TYPE = " TEXT";

    //SQLite can save dates as TEXT, REAL or INTEGER
    private static final String DATE_TYPE = " TEXT";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserPersistenceContract.UserEntry.TABLE_NAME + " (" +
                    UserPersistenceContract.UserEntry.COLUMN_NAME_USER_ID + LONG_TYPE + " PRIMARY KEY," +
                    UserPersistenceContract.UserEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    UserPersistenceContract.UserEntry.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                    UserPersistenceContract.UserEntry.COLUMN_NAME_BIRTHDAY + DATE_TYPE +
            " )";

    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}
