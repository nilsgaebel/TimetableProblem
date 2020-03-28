package com.company;

public class StudentGroup_Blacklist {

    private int idDay;
    private int idTimeSlot;
    private String idStudentGroup;

    public StudentGroup_Blacklist(int idDay, int idTimeSlot, String idStudentGroup) {
        this.idDay = idDay;
        this.idTimeSlot = idTimeSlot;
        this.idStudentGroup = idStudentGroup;
    }

    public String getIdStudentGroup() {
        return idStudentGroup;
    }

    public int getIdTimeSlot() {
        return idTimeSlot;
    }

    public int getIdDay() {
        return idDay;
    }
}
