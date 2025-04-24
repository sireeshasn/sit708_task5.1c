package com.example.itube;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "playlist.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_VIDEOS = "playlist";

    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_FULLNAME = "fullname";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    private static final String COLUMN_VIDEO_ID = "id";
    private static final String COLUMN_VIDEO_URL = "video_url";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FULLNAME + " TEXT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";

        String CREATE_VIDEOS_TABLE = "CREATE TABLE " + TABLE_VIDEOS + " (" +
                COLUMN_VIDEO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_VIDEO_URL + " TEXT)";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_VIDEOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        onCreate(db);
    }

    // Insert a video URL
    public boolean insertVideo(String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_VIDEO_URL, url);
        long result = db.insert(TABLE_VIDEOS, null, contentValues);
        return result != -1;
    }

    // Insert a new user
    public boolean insertUser(String fullName, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULLNAME, fullName);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // Check user credentials
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // ✅ Get all saved playlist links as a List<String>
    public List<String> getAllPlaylistLinks() {
        List<String> playlistLinks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_VIDEO_URL + " FROM " + TABLE_VIDEOS, null);

        if (cursor.moveToFirst()) {
            do {
                playlistLinks.add(cursor.getString(0)); // video_url
            } while (cursor.moveToNext());
        }

        cursor.close();
        return playlistLinks;
    }
}
