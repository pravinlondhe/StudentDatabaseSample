package com.pravin.android.studentdatabase;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pravin.android.studentdatabase.model.Student;
import com.pravin.android.studentdatabase.model.StudentDatabase;
import com.pravin.android.studentdatabase.model.StudentDbDao;
import com.pravin.android.studentdatabase.presenter.StudentDbPresenter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String DATABASE_NAME = Environment.getExternalStorageDirectory() + "/STUDENT_DATABASE.db";
    private final static String TABLE_NAME = "STUDENT_TABLE";
    private final static int DATABASE_VERSION = 1;
    private final static int REQUEST_CODE = 100;
    private final static String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mStudentListRv;
    private TextView mAvailableStudentTv;
    private StudentDbDao mDbDao;
    private StudentDatabase mDatabase;
    private List<Student> mList = new ArrayList<>();
    private StudentDbAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = new StudentDatabase(this, DATABASE_NAME, TABLE_NAME, null, DATABASE_VERSION);
        mDbDao = StudentDbDao.getStudentDbDao(mDatabase);
        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mDatabase) {
            mDatabase.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermission();
        initRecyclerView();

    }

    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        mStudentListRv.setLayoutManager(manager);
        mAdapter = new StudentDbAdapter(mList);
        mStudentListRv.setAdapter(mAdapter);

        mList = new ArrayList<>(mDbDao.get50StudentList(0));
        Log.d(TAG, "Got 50 list");
        for (Student s : mList) {
            Log.d(TAG, "Roll No.:" + s.getRollNo() + " Name:" + s.getName());
        }
        mAdapter.newDataAdded(mList);

        mStudentListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItems = manager.getItemCount();
                int lastVisiblePosition = manager.findLastVisibleItemPosition();
                Log.d(TAG, "totalItems:" + totalItems);
                Log.d(TAG, "LastVisiblePos:" + lastVisiblePosition);
                if (lastVisiblePosition >= totalItems - 1) {
                    loadMoreItems(lastVisiblePosition);
                }

            }
        });


    }

    private void loadMoreItems(final int index) {

        mList = new ArrayList<>(mDbDao.get10StudentList(index + 1));
        Log.d(TAG, "Got 10 list");
        for (Student s : mList) {
            Log.d(TAG, "Roll No.:" + s.getRollNo() + " Name:" + s.getName());
        }
        mAdapter.loaded10MoreItems(mList, index);
    }

    private void initViews() {
        mStudentListRv = findViewById(R.id.rv_student_list);
        mAvailableStudentTv = findViewById(R.id.tv_available_list);
        FloatingActionButton addStudentFab = findViewById(R.id.fab_add_student);
        addStudentFab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_add_student:
                showAddStudentDialog();
                break;
            default:
                // default on click listener case
        }
    }

    private void showAddStudentDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("add_student_dialog");
        if (null != fragment) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
        AddStudentDialog dialog = new AddStudentDialog();
        dialog.show(fragmentManager, "add_student_dialog");
    }


    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            onResume();
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            insertStudentNames();
        }
    }

    public void insertStudentNames() {
        HandlerThread handlerThread = new HandlerThread("generator");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                File file = new File(Environment.getExternalStorageDirectory() + "/student_names.txt");
                try {
                    FileReader fileReader = new FileReader(file);
                    BufferedReader reader = new BufferedReader(fileReader);
                    String name;
                    while ((name = reader.readLine()) != null) {
                        if (!name.isEmpty()) {
                            StudentDbPresenter.getPresenterInstance().addDetailToDb(getBaseContext(), name);
                        }
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
