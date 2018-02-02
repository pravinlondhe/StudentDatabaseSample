package com.pravin.android.studentdatabase;

import android.content.Context;

import com.pravin.android.studentdatabase.model.Student;

import java.util.List;

/**
 * Created by pravin on 1/2/18.
 */

public interface StudentDbContract {

    interface StudentDbView {
        void updateStudentList(List<Student> list);
    }

    interface Presenter {
        boolean addDetailToDb(Context context, String name);
    }

    interface OnLoadMoreListener {
        void onLoadMore();
    }
}
