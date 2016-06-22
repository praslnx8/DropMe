/*
 * Copyright 2015 Anton Tananaev (anton.tananaev@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.prasilabs.dropme.db.dbmanage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.prasilabs.dropme.customs.ValueMapper;
import com.prasilabs.dropme.db.dbPojos.DropMeNotifs;
import com.prasilabs.dropme.debug.ConsoleLog;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "traccar.db";
    private static final String NOTIF_TABLE_NAME = "notif_table";
    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NOTIF_TABLE_NAME + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "jobType TEXT," +
                "message TEXT," +
                "isRead INTEGER," +
                "createdTime INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NOTIF_TABLE_NAME + ";");
        onCreate(db);
    }

    private Long insertNotification(DropMeNotifs dropMeNotifs) {
        try {
            ContentValues values = new ContentValues();
            values = ValueMapper.mapObjectToContentValues(values, dropMeNotifs);

            return db.insertOrThrow(NOTIF_TABLE_NAME, null, values);
        } catch (Exception e) {
            ConsoleLog.e(e);
        }

        return null;
    }

    public void insertNotificationAsync(final DropMeNotifs dropMeNotifs, DatabaseHandler<Long> handler) {
        new DatabaseAsyncTask<Long>(handler) {
            @Override
            protected Long executeMethod() {
                return insertNotification(dropMeNotifs);
            }
        }.execute();
    }

    private List<DropMeNotifs> getNotifList() {
        List<DropMeNotifs> dropMeNotifsList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + NOTIF_TABLE_NAME + " ORDER BY createdTime", null);
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    DropMeNotifs dropMeNotifs = new DropMeNotifs();
                    ValueMapper.mapCursortoObject(cursor, dropMeNotifs);

                    dropMeNotifsList.add(dropMeNotifs);
                }
            }
        } catch (Exception e) {
            ConsoleLog.e(e);
        } finally {
            cursor.close();
        }

        return dropMeNotifsList;
    }

    public void getNotifListAsync(DatabaseHandler<List<DropMeNotifs>> handler) {
        new DatabaseAsyncTask<List<DropMeNotifs>>(handler) {
            @Override
            protected List<DropMeNotifs> executeMethod() {
                return getNotifList();
            }
        }.execute();
    }

    private void deleteNotifs(long id) {
        if (db.delete(NOTIF_TABLE_NAME, "id = ?", new String[]{String.valueOf(id)}) != 1) {
            throw new SQLException();
        }
    }

    public void deleteNotifsAsync(final long id, DatabaseHandler<Void> handler) {
        new DatabaseAsyncTask<Void>(handler) {
            @Override
            protected Void executeMethod() {
                deleteNotifs(id);
                return null;
            }
        }.execute();
    }

    public interface DatabaseHandler<T> {
        void onComplete(boolean success, T result);
    }

    private static abstract class DatabaseAsyncTask<T> extends AsyncTask<Void, Void, T> {

        private DatabaseHandler<T> handler;
        private RuntimeException error;

        public DatabaseAsyncTask(DatabaseHandler<T> handler) {
            this.handler = handler;
        }

        @Override
        protected T doInBackground(Void... params) {
            try {
                return executeMethod();
            } catch (RuntimeException error) {
                this.error = error;
                return null;
            }
        }

        protected abstract T executeMethod();

        @Override
        protected void onPostExecute(T result) {
            handler.onComplete(error == null, result);
        }
    }

}
