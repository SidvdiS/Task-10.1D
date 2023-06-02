package com.example.task61d.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.task61d.model.User;
import com.example.task61d.util.UserUtil;

import java.io.ByteArrayOutputStream;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imgInBytes = null;
    public UserDatabaseHelper(Context context) {
        super(context, UserUtil.DATABASE_NAME, null, UserUtil.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + UserUtil.TABLE_NAME + "(" +
                UserUtil.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserUtil.USERNAME + " TEXT UNIQUE, " +
                UserUtil.FULL_NAME + " TEXT, " +
                UserUtil.PASSWORD + " TEXT, " +
                UserUtil.PHONE_NO + " TEXT, " +
                UserUtil.USER_IMAGE + " BLOB" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+UserUtil.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long insertUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        Bitmap userImageBitmap = user.getUserImage();
        if(userImageBitmap!=null) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            userImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            imgInBytes = byteArrayOutputStream.toByteArray();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(UserUtil.USERNAME, user.getUsername());
        contentValues.put(UserUtil.FULL_NAME, user.getFullName());
        contentValues.put(UserUtil.PASSWORD, user.getPassword());
        contentValues.put(UserUtil.PHONE_NO, user.getPhoneNumber());
        contentValues.put(UserUtil.USER_IMAGE, imgInBytes);

        long insert = db.insert(UserUtil.TABLE_NAME, null, contentValues);
        return insert;
    }

    public boolean isValidLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + UserUtil.TABLE_NAME +
                        " WHERE " + UserUtil.USERNAME + " = ? AND " + UserUtil.PASSWORD + " = ?",
                new String[]{username, password});

        boolean isValidLogin = false;
        if (cursor != null && cursor.moveToFirst()) {
            // The cursor contains at least one row, indicating a successful login
            isValidLogin = true;
            cursor.close();
        }

        return isValidLogin;
    }

    public boolean isUserRegistered(String username) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.rawQuery(
                "SELECT * FROM " + UserUtil.TABLE_NAME +
                        " WHERE " + UserUtil.USERNAME + " = ?",
                new String[]{username})) {

            boolean isUserRegistered = false;
            if (cursor != null && cursor.moveToFirst()) {
                isUserRegistered = true;
            }
            return isUserRegistered;
        }
    }
}
