package com.company;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.*;

public class Main {

    public static void main(String[] args) {

        StoredDataObjects storedDataObjects = new StoredDataObjects();
        storedDataObjects.readDataObjects(args[0]);

        ArrayList<Lecture> allLectures = storedDataObjects.allLecturesToSchedule;
        ArrayList<Lecture> lecturesToSchedule = new ArrayList<>(allLectures);
        ArrayList<Lecture> lecturesScheduled = new ArrayList<>();



        List<ClassroomAndTime> allClassroomAndTimes = storedDataObjects.allClassroomAndTime;

        List<ClassroomAndTime> allClassroomAndTimesForLecture;

        for (Lecture lec : lecturesToSchedule) {
            allClassroomAndTimesForLecture = lec.getApplicableTimeslots();
            if (lec.getApplicableTimeslots().size() == 0) {
                System.out.println("lec : " + lec.getIdLecture() + " kein TimeSlot mehr buchbar. ");
                break;
            }
            for (ClassroomAndTime currentForLec:allClassroomAndTimesForLecture) {
                if(allClassroomAndTimes.contains(currentForLec)){
                    lec.setScheduledClassroomAndTime(currentForLec);
                    storedDataObjects.allLecturesToSchedule.remove(lec);
                    storedDataObjects.updateAvailableClassroomAndTimesForEachLecture(lec,currentForLec);
                    allClassroomAndTimes.remove(currentForLec);
                    lecturesScheduled.add(lec);
                    break;
                }
            }
        }
    }
}
