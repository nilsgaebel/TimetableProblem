package com.company;

public class ClassroomAndTime {

    private int idClassroom;
    private int capacity;
    private int idDay;
    private int idTimeSlot;

    public ClassroomAndTime(int idClassroom, int capacity, int idDay, int idTimeSlot) {
        this.idClassroom = idClassroom;
        this.capacity = capacity;
        this.idDay = idDay;
        this.idTimeSlot = idTimeSlot;
    }

    public int getIdClassroom() {
        return idClassroom;
    }

    public int getIdDay() {
        return idDay;
    }

    public int getIdTimeSlot() {
        return idTimeSlot;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof ClassroomAndTime){
            ClassroomAndTime p = (ClassroomAndTime) o;
            return (this.getIdTimeSlot() == p.getIdTimeSlot() && this.getIdDay() == p.getIdDay() && this.getIdClassroom() == p.getIdClassroom());
        } else
            return false;
    }
}
