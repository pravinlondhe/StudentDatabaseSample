package com.pravin.android.studentdatabase.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pravin on 1/2/18.
 */

public class StudentDbDao {


    private static final String STUDENT_NAME_COL_NAME = "student_name";
    private static final String ROLL_NO_COL_NAME = "roll_no";
    private final static String TAG = StudentDbDao.class.getSimpleName();
    private static StudentDatabase mStudentDatabase;
    private static StudentDbDao mStudentDbDao;
    private List<Student> list = new ArrayList<>();

    private StudentDbDao() {
        //Default constructor
    }


    public static StudentDbDao getStudentDbDao(StudentDatabase studentDatabase) {
        mStudentDatabase = studentDatabase;
        if (null == mStudentDbDao) {
            mStudentDbDao = new StudentDbDao();
        }
        return mStudentDbDao;
    }

    public synchronized boolean addStudent(String studentName) {
        if (null == studentName) {
            return false;
        }
        SQLiteDatabase database = mStudentDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDENT_NAME_COL_NAME, studentName);
        long val = database.insert(mStudentDatabase.getTableName(), null, contentValues);
        return val != -1;

    }

    public synchronized List<Student> get50StudentList() {
        list.clear();
        String readQuery = "SELECT " + ROLL_NO_COL_NAME + "," + STUDENT_NAME_COL_NAME + " FROM " + mStudentDatabase.getTableName() + " ORDER BY " + ROLL_NO_COL_NAME;
        SQLiteDatabase database = mStudentDatabase.getWritableDatabase();
        Cursor cursor = database.rawQuery(readQuery, null);
        Log.d(TAG, "Cursor:" + cursor.getCount());
        if (cursor.getCount() != 0 && cursor.moveToPosition(0)) {
            while (!cursor.isAfterLast() && list.size() < 50) {
                int rollNo = cursor.getInt(cursor.getColumnIndex(ROLL_NO_COL_NAME));
                String name = cursor.getString(cursor.getColumnIndex(STUDENT_NAME_COL_NAME));
                list.add(new Student(rollNo, name));
                cursor.moveToNext();
            }
        }

        cursor.close();

        return list;
    }

    public synchronized List<Student> get10StudentList(int startIndex) {
        list.clear();
        String readQuery = "SELECT * FROM " + mStudentDatabase.getTableName()
                + " WHERE " + ROLL_NO_COL_NAME + " >=" + startIndex
                + " ORDER BY " + ROLL_NO_COL_NAME
                + " LIMIT 10";
        SQLiteDatabase database = mStudentDatabase.getReadableDatabase();
        Cursor cursor = database.rawQuery(readQuery, null);
        if (null != cursor && cursor.moveToPosition(0)) {
            while (!cursor.isAfterLast() && list.size() < 10) {
                int rollNo = cursor.getInt(cursor.getColumnIndex(ROLL_NO_COL_NAME));
                String name = cursor.getString(cursor.getColumnIndex(STUDENT_NAME_COL_NAME));
                list.add(new Student(rollNo, name));
                cursor.moveToNext();
            }
        }
        if (null != cursor) {
            cursor.close();
        }
        return list;
    }

}
