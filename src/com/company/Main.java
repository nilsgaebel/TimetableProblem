package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.*;

public class Main {

    public static void main(String[] args) {

        StoredDataObjects storedDataObjects = new StoredDataObjects();
        storedDataObjects.readDataObjects(args[0]);

        ArrayList<Lecture> allLectures = storedDataObjects.allLectures;
        ArrayList<Lecturer> allLecturer = storedDataObjects.allLecturers;
        ArrayList<Lecturer_Blacklist> allLecturer_Blacklists = storedDataObjects.allLecturer_Blacklists;
        ArrayList<ClassroomAndTime> allClassroomAndTimes = storedDataObjects.getAvailableClassroomAndTimes();
        ArrayList<ClassroomAndTime> possibleClassroomAndTimesForLecture = storedDataObjects.getAvailableClassroomAndTimes();
        List<ClassroomAndTime> selectedClassroomAndTime;

        for (Lecture lecture:allLectures) {
            if(lecture.getIdLecture() == 9){
                System.out.println();
            }
            Lecturer lecturerForLecture = allLecturer.stream().filter(id -> id.getIdLecturer() == lecture.getIdLecturer()).findAny().get();
            List<Lecturer_Blacklist> lecturer_blacklist = allLecturer_Blacklists.stream().filter(id -> id.getIdLecturer() == lecturerForLecture.getIdLecturer()).collect(Collectors.toList());
            if(lecturer_blacklist.size()>0){
                for (Lecturer_Blacklist iterator: lecturer_blacklist) {
                    selectedClassroomAndTime = allClassroomAndTimes.stream().filter(timeSlot -> timeSlot.getIdTimeSlot() == iterator.getIdTimeSlot() && timeSlot.getIdDay() == iterator.getIdDay()).collect(Collectors.toList());

                    for (ClassroomAndTime iteratorForDelete:selectedClassroomAndTime) {
                        possibleClassroomAndTimesForLecture.remove(iteratorForDelete);
                    }
                }
            }

        }

    }
}
