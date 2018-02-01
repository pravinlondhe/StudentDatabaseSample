package com.pravin.android.studentdatabase.model;

/**
 * Created by pravin on 1/2/18.
 */

public class Student {

    private String name;
    private int rollNo;

    public Student(int rollNo, String name) {
        this.name = name;
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public int getRollNo() {
        return rollNo;
    }
}
