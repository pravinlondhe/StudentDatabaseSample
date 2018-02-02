package com.pravin.android.studentdatabase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pravin.android.studentdatabase.presenter.StudentDbPresenter;

/**
 * Created by pravin on 1/2/18.
 */

public class AddStudentDialog extends DialogFragment implements View.OnClickListener {
    //    private final static String TAG = AddStudentDialog.class.getSimpleName();
    private EditText mStudentNameEt;
    private TextView mSubmitTv;
    private TextView mCancelTv;

    public AddStudentDialog() {
        //default constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_add_student, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSubmitTv.setOnClickListener(this);
        mCancelTv.setOnClickListener(this);
    }

    private void initViews(View view) {
        mStudentNameEt = view.findViewById(R.id.et_student_name);
        mSubmitTv = view.findViewById(R.id.bt_submit);
        mCancelTv = view.findViewById(R.id.bt_cancel);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
                addDetailToDb();
                break;
            case R.id.bt_cancel:
                getDialog().dismiss();
                break;
            default:
                // default add student dialog
        }
    }

    private void addDetailToDb() {
        if (!mStudentNameEt.getText().toString().isEmpty()) {
            if (StudentDbPresenter.getPresenterInstance().addDetailToDb(getActivity(), mStudentNameEt.getText().toString())) {
                displayLog(getString(R.string.toast_record_added));
            } else {
                displayLog(getString(R.string.toast_error_in_adding));
            }
            getDialog().dismiss();
        } else {
            displayLog(getString(R.string.toast_invalid_input));
        }
    }

    private void displayLog(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

}
