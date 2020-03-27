package com.company;

public class Classroom {

    private int idClassroom;
    private String name;
    private int capacity;

    public Classroom(int idClassroom, String name, int capacity) {
        this.idClassroom = idClassroom;
        this.name = name;
        this.capacity = capacity;
    }


    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getIdClassroom() {
        return idClassroom;
    }
}
