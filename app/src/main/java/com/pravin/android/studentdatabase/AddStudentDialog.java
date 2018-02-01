package com.pravin.android.studentdatabase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by pravin on 1/2/18.
 */

public class AddStudentDialog extends DialogFragment implements View.OnClickListener {

    private EditText mStudentNameEt;
    private TextView mSubmitTv;
    private TextView mCancelTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_add_student, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mStudentNameEt = view.findViewById();
        mSubmitTv = view.findViewById();
        mSubmitTv.setOnClickListener(this);
        mCancelTv.setOnClickListener(this);
        mCancelTv = view.findViewById();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case
        }
    }
}
