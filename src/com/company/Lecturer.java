package com.company;

public class Lecturer {

    private int idLecturer;
    private String name;

    public Lecturer(int idLecturer, String name) {
        this.idLecturer = idLecturer;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getIdLecturer() {
        return idLecturer;
    }
}
