package com.company;

import java.util.ArrayList;

public class Lecture {

    private int idLectureObject;
    private int idLecture;
    private int idLecturer;

    private ArrayList<ClassroomAndTime> applicableTimeslots;

    public Lecture(int idLectureObject, int idLecture, int idLecturer) {
        this.idLectureObject = idLectureObject;
        this.idLecture = idLecture;
        this.idLecturer = idLecturer;
    }

    public void setApplicableTimeslots(){

    }

    public int getIdLecturer() {
        return idLecturer;
    }

    public int getIdLecture() {
        return idLecture;
    }

    public int getIdLectureObject() {
        return idLectureObject;
    }
}
