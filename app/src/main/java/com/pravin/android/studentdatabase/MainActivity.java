package com.pravin.android.studentdatabase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pravin.android.studentdatabase.R;
import com.pravin.android.studentdatabase.model.StudentDatabase;
import com.pravin.android.studentdatabase.model.StudentDbDao;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String DATABASE_NAME = "STUDENT_DATABASE";
    private final static String TABLE_NAME = "STUDENT_TABLE";
    private final static int DATABASE_VERSION = 1;
    private RecyclerView mStudentListRv;
    private TextView mAvailableStudentTv;
    private FloatingActionButton mAddStudentFab;
    private StudentDbDao mDbDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        StudentDatabase database = new StudentDatabase(this, DATABASE_NAME, TABLE_NAME, null, DATABASE_VERSION);
        mDbDao = StudentDbDao.getStudentDbDao(this, database);
    }

    private void initViews() {
        mStudentListRv = findViewById(R.id.rv_student_list);
        mAvailableStudentTv = findViewById(R.id.tv_available_list);
        mAddStudentFab = findViewById(R.id.fab_add_student);
        mAddStudentFab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_add_student:
                break;
            default:
                // default on click listener case
        }
    }
}
