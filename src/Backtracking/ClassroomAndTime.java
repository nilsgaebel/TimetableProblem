package Backtracking;

public class ClassroomAndTime {

    private int idClassroom;
    private int idDay;
    private int idTimeSlot;

    private int indexLecObject;

    public ClassroomAndTime(int idClassroom, int idDay, int idTimeSlot) {
        this.idClassroom = idClassroom;
        this.idDay = idDay;
        this.idTimeSlot = idTimeSlot;
    }

    //Konstruktor wird benutzt für die Zuordnung der gelöschten Zeitslots zum Lecture beim ForwardChecking
    public ClassroomAndTime(int indexLecObject, int idClassroom, int idDay, int idTimeSlot) {
        this.indexLecObject = indexLecObject;
        this.idClassroom = idClassroom;
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

    @Override
    public boolean equals(Object o){
        if(o instanceof ClassroomAndTime){
            ClassroomAndTime p = (ClassroomAndTime) o;
            return (this.getIdTimeSlot() == p.getIdTimeSlot() && this.getIdDay() == p.getIdDay() && this.getIdClassroom() == p.getIdClassroom());
        } else
            return false;
    }

    public int getIndexLecObject() {
        return indexLecObject;
    }

    public void setIndexLecObject(int indexLecObject) {
        this.indexLecObject = indexLecObject;
    }
}
