package com.company;

public class Classroom_Blacklist {

    private int idDay;
    private int idTimeSlot;
    private int idClassroom;

    public Classroom_Blacklist(int idDay, int idTimeSlot, int idClassroom) {
        this.idDay = idDay;
        this.idTimeSlot = idTimeSlot;
        this.idClassroom = idClassroom;
    }

    public int getIdDay() {
        return idDay;
    }

    public int getIdTimeSlot() {
        return idTimeSlot;
    }

    public int getIdClassroom() {
        return idClassroom;
    }
}
