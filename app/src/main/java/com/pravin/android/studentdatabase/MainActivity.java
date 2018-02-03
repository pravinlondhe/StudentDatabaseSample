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

    private final static String DATABASE_NAME = "STUDENT_DATABASE.db";
    private final static String TABLE_NAME = "STUDENT_TABLE";
    private final static int DATABASE_VERSION = 1;
    private final static int REQUEST_CODE = 100;
    private final static String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mStudentListRv;
    private StudentDbDao mDbDao;
    private StudentDatabase mDatabase;
    private List<Student> mList = new ArrayList<>();
    private StudentDbAdapter mAdapter;
    private volatile boolean isLoadingDown;
    private volatile boolean isLoadingUp;
    private volatile int mPage = 5;
    private volatile int totalItems;
    private volatile int lastVisiblePosition;
    private volatile int firstVisiblePosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initRecyclerView();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        checkDbExits();
        requestPermission();
        mDatabase = new StudentDatabase(this, DATABASE_NAME, TABLE_NAME, null, DATABASE_VERSION);
        mDbDao = StudentDbDao.getStudentDbDao(mDatabase);
        HandlerThread handlerThread = new HandlerThread("loading");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mList = new ArrayList<>(mDbDao.get50StudentList());
                Log.d(TAG, "Got 50 list");
                for (Student s : mList) {
                    Log.d(TAG, "Roll No.:" + s.getRollNo() + " Name:" + s.getName());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Updating RV");
                        mAdapter.newDataAdded(mList);
                    }
                });
            }
        }, 1500);
    }

    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        mStudentListRv.setLayoutManager(manager);
        mAdapter = new StudentDbAdapter(mList);
        mStudentListRv.setAdapter(mAdapter);

        mStudentListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                Log.d(TAG,"x position:"+dx+" y position:"+dy);

                totalItems = manager.getItemCount();
                lastVisiblePosition = manager.findLastVisibleItemPosition();

                if (dx <= dy) { //down scroll
                    Log.d(TAG, "totalItems:" + totalItems + " LastVisiblePos:" + lastVisiblePosition);
//                    Log.d(TAG, "isLoading:" + isLoading);
                    if (!isLoadingDown && lastVisiblePosition >= totalItems - 5) {
                        Log.d(TAG, "Loading new dataset");
                        isLoadingDown = true;
                        loadMoreDownItems();
                    }
                } else if (dx >= dy) { // up scroll
                    firstVisiblePosition = manager.findFirstVisibleItemPosition();
                    Log.d(TAG, "totalItems:" + totalItems + " FirstVisiblePos:" + firstVisiblePosition
                            + " mPage:" + mPage + " isLoadingUp:" + isLoadingUp);
//                    Log.d(TAG, "isLoading:" + isLoading+" LastVisiblePos:" + lastVisiblePosition);
                    int startIndex = ((mPage - 5) * 10) - 9;
                    if (!isLoadingUp && firstVisiblePosition <= 5 && startIndex > 0) {
                        Log.d(TAG, "Loading new dataset");
                        isLoadingUp = true;
                        loadMoreUpItems();
                    }
                }
            }
        });
    }

    private void loadMoreUpItems() {
        final int startPoint = ((mPage - 5) * 10) - 9;
        Log.d(TAG, "Up start pt:" + startPoint);

        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                mList = new ArrayList<>(mDbDao.get10StudentList(startPoint));
//                Log.d(TAG, "Got 10 Up list");
//                for (Student s : mList) {
//                    Log.d(TAG, "Roll No.:" + s.getRollNo() + " Name:" + s.getName());
//                }
                mPage--;
                if (mList.size() > 0) {
                    mAdapter.loaded10MoreUpItems(mList);
                    refreshUpList();

                }
            }
        });

    }

    private void loadMoreDownItems() {
        Log.d(TAG, "Down start pt:" + ((mPage * 10) + 1));

        mAdapter.displayLoadingItem();
        mStudentListRv.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyItemInserted(51);
            }
        });

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                mList = new ArrayList<>(mDbDao.get10StudentList(((mPage * 10) + 1)));
                Log.d(TAG, "Got 10 list");
                for (Student s : mList) {
                    Log.d(TAG, "Roll No.:" + s.getRollNo() + " Name:" + s.getName());
                }
                mAdapter.removeLoadingItem();
                mStudentListRv.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyItemRemoved(51);
                    }
                });

                mPage++;
                if (mList.size() > 0) {
                    mAdapter.loaded10MoreDownItems(mList);
                    refreshDownList();
                }
            }
        }, 1000);

    }

    private void initViews() {
        mStudentListRv = findViewById(R.id.rv_student_list);
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
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

    private void insertStudentNames() {
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

    private void refreshDownList() {
        mStudentListRv.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyItemRangeRemoved(0, 10);
                mAdapter.notifyItemRangeInserted(51, 60);
                isLoadingDown = false;

            }
        });
//        mStudentListRv.smoothScrollToPosition(40);
    }

    private void refreshUpList() {
        mStudentListRv.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyItemRangeRemoved(50, 60);
                mAdapter.notifyItemRangeInserted(0, 10);
                isLoadingUp = false;
            }
        });
    }

    private void checkDbExits() {
        File file = new File(Environment.getDataDirectory() + "/data/" +
                getPackageName() + "/databases/" + DATABASE_NAME);
        if (!file.exists()) {
            insertStudentNames();
        }
    }
}
