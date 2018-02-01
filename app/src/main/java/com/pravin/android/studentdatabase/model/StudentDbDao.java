package com.pravin.android.studentdatabase.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pravin on 1/2/18.
 */

public class StudentDbDao {

    private static final String STUDENT_NAME_COL_NAME = "student_name";
    private static final String ROLL_NO_COL_NAME = "roll_no";
    private static StudentDatabase mStudentDatabase;
    private static StudentDbDao mStudentDbDao;

    private StudentDbDao() {
        //Default constructor
    }

    public static StudentDbDao getStudentDbDao(Context context, StudentDatabase studentDatabase) {
        mStudentDatabase = studentDatabase;
        if (null == mStudentDbDao) {
            mStudentDbDao = new StudentDbDao();
        }
        return mStudentDbDao;
    }

    public void addStudent(String studentName) {
        if (null == studentName) {
            return;
        }
        SQLiteDatabase database = mStudentDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDENT_NAME_COL_NAME, studentName);
        database.insert(mStudentDatabase.getTableName(), null, contentValues);
    }

    public List<Student> get50StudentList(int startIndex) {
        List<Student> list = new ArrayList<>();
        String readQuery = "SELECT * FROM " + mStudentDatabase.getTableName() + " ORDER BY " + ROLL_NO_COL_NAME;
        SQLiteDatabase database = mStudentDatabase.getReadableDatabase();
        Cursor cursor = database.rawQuery(readQuery, null);
        if (null != cursor && cursor.moveToPosition(startIndex)) {
            while (!cursor.isAfterLast() && list.size() <= 50) {
                int rollNo = cursor.getInt(cursor.getColumnIndex(ROLL_NO_COL_NAME));
                String name = cursor.getString(cursor.getColumnIndex(STUDENT_NAME_COL_NAME));
                list.add(new Student(rollNo, name));
            }
        }
        if (null != cursor) {
            cursor.close();
        }
        return list;
    }

    public List<Student> get10StudentList(int startIndex) {
        List<Student> list = new ArrayList<>();
        String readQuery = "SELECT * FROM " + mStudentDatabase.getTableName() + " ORDER BY " + ROLL_NO_COL_NAME;
        SQLiteDatabase database = mStudentDatabase.getReadableDatabase();
        Cursor cursor = database.rawQuery(readQuery, null);
        if (null != cursor && cursor.moveToPosition(startIndex)) {
            while (!cursor.isAfterLast() && list.size() <= 10) {
                int rollNo = cursor.getInt(cursor.getColumnIndex(ROLL_NO_COL_NAME));
                String name = cursor.getString(cursor.getColumnIndex(STUDENT_NAME_COL_NAME));
                list.add(new Student(rollNo, name));
            }
        }
        if (null != cursor) {
            cursor.close();
        }
        return list;
    }
}
