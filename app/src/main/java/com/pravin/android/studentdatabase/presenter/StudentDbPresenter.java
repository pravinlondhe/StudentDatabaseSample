package com.pravin.android.studentdatabase.presenter;

import android.content.Context;
import android.os.Environment;

import com.pravin.android.studentdatabase.StudentDbContract;
import com.pravin.android.studentdatabase.model.StudentDatabase;
import com.pravin.android.studentdatabase.model.StudentDbDao;

/**
 * Created by pravin on 2/2/18.
 */

public class StudentDbPresenter implements StudentDbContract.Presenter {

    private final static String DATABASE_NAME = Environment.getExternalStorageDirectory() + "/STUDENT_DATABASE.db";
    private final static String TABLE_NAME = "STUDENT_TABLE";
    private final static int DATABASE_VERSION = 1;
    private static StudentDbPresenter mPresenter;


    private StudentDbPresenter() {
        //default constructor
    }

    public static StudentDbPresenter getPresenterInstance() {
        if (null == mPresenter) {
            mPresenter = new StudentDbPresenter();
        }
        return mPresenter;
    }

    @Override
    public boolean addDetailToDb(Context context, String name) {
        StudentDatabase database = new StudentDatabase(context, DATABASE_NAME, TABLE_NAME, null, DATABASE_VERSION);
        boolean val = StudentDbDao.getStudentDbDao(database).addStudent(name);
        database.close();
        return val;
    }
}
