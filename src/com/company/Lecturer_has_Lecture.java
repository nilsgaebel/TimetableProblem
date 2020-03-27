package com.company;

//Doppelte Klasse aus dem Datensatz!
public class Lecturer_has_Lecture {

    private int idLecture;
    private String idStudentGroup;
    private int numParticipants;

    public Lecturer_has_Lecture(int idLecture, String idStudentGroup, int numParticipants) {
        this.idLecture = idLecture;
        this.idStudentGroup = idStudentGroup;
        this.numParticipants = numParticipants;
    }
}
