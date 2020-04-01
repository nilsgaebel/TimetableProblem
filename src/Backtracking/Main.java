package Backtracking;

import Data.Lecture;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        StoredDataObjects storedDataObjects = new StoredDataObjects();
        storedDataObjects.readDataObjects(args[0]);

        Backtracking backtracking = new Backtracking(new ArrayList<>(), storedDataObjects.allLecturesToSchedule,storedDataObjects.allLecture_has_StudentGroups);
        backtracking.startBacktracking();
        ArrayList<Lecture> result = backtracking.getResult();

        for (Lecture lec: result) {
            System.out.println("ID LectureObject: " + lec.getIdLectureObject() + " Raum: " + lec.getScheduledClassroomAndTime().getIdClassroom() + " Tag: " + lec.getScheduledClassroomAndTime().getIdDay() + " Timeslot: "+lec.getScheduledClassroomAndTime().getIdTimeSlot() + " Lecturer: " + lec.getIdLecturer());
        }
    }
}
