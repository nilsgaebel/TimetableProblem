package com.company;

public class Lecturer_Blacklist {

    private int idDay;
    private int idTimeSlot;
    private int idLecturer;

    public Lecturer_Blacklist(int idDay, int idTimeSlot, int idLecturer) {
        this.idDay = idDay;
        this.idTimeSlot = idTimeSlot;
        this.idLecturer = idLecturer;
    }

    public int getIdLecturer() {
        return idLecturer;
    }

    public int getIdDay() {
        return idDay;
    }

    public int getIdTimeSlot() {
        return idTimeSlot;
    }
}
