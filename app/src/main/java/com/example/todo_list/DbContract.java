package com.example.todo_list;

// Imports
import android.provider.BaseColumns;

// Final class to deny extending.
public final class DbContract {
    private DbContract () {}

    public static class DbToDoListContract implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME_TASK = "task";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_PRIORITY = "priority";

        // Values for the status of the task
        public static final int STATUS_TODO = 0;
        public static final int STATUS_COMPLETED = 1;

        // Values for the priority of the task
        public static final int PRIORITY_NOT_IMPORTANT = 0;
        public static final int PRIORITY_LESS_IMPORTANT = 1;
        public static final int PRIORITY_IMPORTANT = 2;
        public static final int PRIORITY_VERY_IMPORTANT = 3;

    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DbToDoListContract.TABLE_NAME + " (" +
                    DbToDoListContract._ID + " INTEGER PRIMARY KEY," +
                    DbToDoListContract.COLUMN_NAME_TASK + " TEXT NOT NULL," +
                    DbToDoListContract.COLUMN_NAME_STATUS + " INTEGER," +
                    DbToDoListContract.COLUMN_NAME_PRIORITY + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DbToDoListContract.TABLE_NAME;
}
