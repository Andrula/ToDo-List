package com.example.todo_list;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ToDoList.db";

    // Constructor creates object to help handle a defined database.
    public ToDoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    // Method to mark task as completed.
    public void markTaskAsCompleted(long taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContract.DbToDoListContract.COLUMN_NAME_STATUS, DbContract.DbToDoListContract.STATUS_COMPLETED);

        String selection = DbContract.DbToDoListContract._ID + " =?";
        String[] selectionArgs = { String.valueOf(taskId)};
        db.update(DbContract.DbToDoListContract.TABLE_NAME, values, selection, selectionArgs);
    }


}
