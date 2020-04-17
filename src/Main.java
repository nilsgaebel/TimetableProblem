import Backtracking.Backtracking;
import Backtracking.StoredDataObjects;
import Backtracking.ClassroomAndTime;
import Data.Lecture;
import Data.Lecture_has_StudentGroup;
import Data.Lecturer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        StoredDataObjects storedDataObjects = new StoredDataObjects();
        storedDataObjects.readDataObjects(args[0]);
        ArrayList<Lecture> allLecturesToSchedule = storedDataObjects.allLecturesToSchedule;

        ArrayList<Lecturer> allLecturersToSchedule = storedDataObjects.allLecturers;
        Backtracking backtracking = new Backtracking(new ArrayList<>(), storedDataObjects.allLecturesToSchedule,storedDataObjects.allLecture_has_StudentGroups);
        boolean resultB = backtracking.startBacktracking(true);

        System.out.println("Ergebnis Backtracking: " + resultB);

        ArrayList<Lecture> result = backtracking.getResult();
        for (Lecture lec: result) {
            System.out.println("ID LectureObject: " + lec.getIdLectureObject() + " Raum: " + lec.getScheduledClassroomAndTime().getIdClassroom() + " Tag: " + lec.getScheduledClassroomAndTime().getIdDay() + " Timeslot: "+lec.getScheduledClassroomAndTime().getIdTimeSlot() + " Lecturer: " + lec.getIdLecturer());
        }
        backtracking.writeResultsToCSV();

        noSubjectsAreScheduledToSameRoomAndTime(result);
        onlyApplicableTimeIsAssigned(result, allLecturesToSchedule);
        noLecturerIsScheduledToSameRoomAndTime(result, allLecturersToSchedule);
        noStudentGroupIsScheduledToSameRoomAndTime(result, storedDataObjects.allLecture_has_StudentGroups);

    }

    //Testcases
    public static void onlyApplicableTimeIsAssigned(ArrayList<Lecture> subjectsScheduled, ArrayList<Lecture>subjectsToSchedule) {

        for(Lecture lec : subjectsScheduled) {
            Lecture currentLec = subjectsToSchedule.stream().filter(id -> id.getIdLectureObject() == lec.getIdLectureObject()).findAny().get();
            List<ClassroomAndTime> possibleDomainForLec= currentLec.getApplicableTimeslots();
            if(!possibleDomainForLec.contains(lec.getScheduledClassroomAndTime())) {
                System.out.println("Failed: Assigned time is not applicable for the subject " + lec.getScheduledClassroomAndTime().toString());
                return;
            }
        }
        System.out.println("Passed: Assigned time is one of given applicable times");
    }

    public static void noSubjectsAreScheduledToSameRoomAndTime(ArrayList<Lecture> subjectsScheduled) {
        ArrayList<ClassroomAndTime> scheduledSlots = new ArrayList<ClassroomAndTime>();
        for (Lecture lec : subjectsScheduled) {
            if (scheduledSlots.contains(lec.getScheduledClassroomAndTime())) {
                System.out.println("Failed: two or more subjects are scheduled at " + lec.getScheduledClassroomAndTime().toString());
                return;
            } else {
                scheduledSlots.add(lec.getScheduledClassroomAndTime());
            }
        }
        System.out.println("Passed: No subjects are assigned to same room and time");
    }

    public static void noLecturerIsScheduledToSameRoomAndTime(ArrayList<Lecture> subjectsScheduled, ArrayList<Lecturer> allLecturers) {
        ArrayList<ClassroomAndTime> scheduledSlots = new ArrayList<ClassroomAndTime>();
        List<Lecture> slectedLecs;
        for(Lecturer lecturer: allLecturers){
            slectedLecs = subjectsScheduled.stream().filter(id -> id.getIdLecturer() == lecturer.getIdLecturer()).collect(Collectors.toList());
            for (Lecture lec : slectedLecs) {
                for(ClassroomAndTime t:scheduledSlots){
                    if(t.getIdDay() == lec.getScheduledClassroomAndTime().getIdDay() && t.getIdTimeSlot() == lec.getScheduledClassroomAndTime().getIdTimeSlot()){
                        System.out.println("Failed: two or more lecturers are scheduled at " + lec.getScheduledClassroomAndTime().toString());
                        return;
                    }else{
                        scheduledSlots.add(lec.getScheduledClassroomAndTime());
                    }
                }
            }
            scheduledSlots.clear();
        }
        System.out.println("Passed: No Lecturers are assigned to same room and time");
    }

    public static void noStudentGroupIsScheduledToSameRoomAndTime(ArrayList<Lecture> subjectsScheduled, ArrayList<Lecture_has_StudentGroup> allStudentgroups) {
        ArrayList<ClassroomAndTime> scheduledSlots = new ArrayList<ClassroomAndTime>();
        List<Lecture> slectedLecs;
        for(Lecture_has_StudentGroup studentGroup: allStudentgroups){
            slectedLecs = subjectsScheduled.stream().filter(id -> id.getIdLecture() == studentGroup.getIdLecture()).collect(Collectors.toList());
            for (Lecture lec : slectedLecs) {
                if (scheduledSlots.contains(lec.getScheduledClassroomAndTime())) {
                    System.out.println("Failed: two or more Studengroups are scheduled at " + lec.getScheduledClassroomAndTime().toString());
                    return;
                } else {
                    scheduledSlots.add(lec.getScheduledClassroomAndTime());
                }
            }
            scheduledSlots.clear();
        }
        System.out.println("Passed: No Studentgroups are assigned to same room and time");
    }
}
