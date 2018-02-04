package com.pravin.android.studentdatabase.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StudentDatabase extends SQLiteOpenHelper {

    private static String mTableName;

    public StudentDatabase(Context context, String databaseName, String tableName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, databaseName, factory, version);
        mTableName = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableQuery = "CREATE TABLE " + mTableName + " (roll_no INTEGER PRIMARY KEY AUTOINCREMENT,student_name TEXT NOT NULL)";
        sqLiteDatabase.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXITS " + mTableName;
        sqLiteDatabase.execSQL(dropTableQuery);
        onCreate(sqLiteDatabase);
    }

    public String getTableName() {
        return mTableName;
    }
}
