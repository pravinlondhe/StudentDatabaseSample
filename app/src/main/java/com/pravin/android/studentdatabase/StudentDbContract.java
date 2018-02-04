package com.pravin.android.studentdatabase;

import android.content.Context;

public interface StudentDbContract {

    interface Presenter {
        boolean addDetailToDb(Context context, String name);
    }
}
