package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StoredDataObjects {

    ArrayList<Classroom> allClassrooms;
    ArrayList<Classroom_Blacklist> allClassroom_Blacklists;
    ArrayList<Lecture> allLectures;
    ArrayList<Lecture_has_StudentGroup> allLecture_has_StudentGroups;
    ArrayList<Lecturer> allLecturers;
    ArrayList<Lecturer_Blacklist> allLecturer_Blacklists;
    ArrayList<StudentGroup> allStudentGroups;
    ArrayList<StudentGroup_Blacklist> allStudentGroup_Blacklist;

    public StoredDataObjects() {
        this.allClassrooms = new ArrayList<>();
        this.allClassroom_Blacklists = new ArrayList<>();
        this.allLectures = new ArrayList<>();
        this.allLecture_has_StudentGroups = new ArrayList<>();
        this.allLecturers = new ArrayList<>();
        this.allLecturer_Blacklists = new ArrayList<>();
        this.allStudentGroups = new ArrayList<>();
        this.allStudentGroup_Blacklist = new ArrayList<>();
    }

    public void readDataObjects(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (File fileEntry : listOfFiles) {
            if (isValidFile(fileEntry.getName())) {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileEntry.getPath()), "UTF-16"));
                    String line = br.readLine(); //skip line with header;
                    String[] lineSplit; //mit File.readAllLines evtl. besser
                    while ((line = br.readLine()) != null && line.length() > 0) {
                        lineSplit = line.split("\t");
                        switch (fileEntry.getName()) {
                            case "Classroom.txt":
                                Classroom currentClassroom = new Classroom(Integer.parseInt(lineSplit[0]), lineSplit[1], Integer.parseInt(lineSplit[2]));
                                this.allClassrooms.add(currentClassroom);
                                break;
                            case "Classroom_Blacklist.txt":
                                Classroom_Blacklist currentClassroom_Blacklist = new Classroom_Blacklist(Integer.parseInt(lineSplit[0]), Integer.parseInt(lineSplit[1]), Integer.parseInt(lineSplit[2]));
                                this.allClassroom_Blacklists.add(currentClassroom_Blacklist);
                                break;
                            case "Lecture.txt":
                                Lecture currentLecture = new Lecture(Integer.parseInt(lineSplit[0]), Integer.parseInt(lineSplit[1]), Integer.parseInt(lineSplit[2]));
                                this.allLectures.add(currentLecture);
                                break;
                            case "Lecture_has_StudentGroup.txt":
                                Lecture_has_StudentGroup currentLecture_has_StudentGroup = new Lecture_has_StudentGroup(Integer.parseInt(lineSplit[0]), lineSplit[1], Integer.parseInt(lineSplit[2]));
                                this.allLecture_has_StudentGroups.add(currentLecture_has_StudentGroup);
                                break;
                            case "Lecturer.txt":
                                Lecturer currentLecturer = new Lecturer(Integer.parseInt(lineSplit[0]), lineSplit[1]);
                                this.allLecturers.add(currentLecturer);
                                break;
                            case "Lecturer_Blacklist.txt":
                                Lecturer_Blacklist currentLecturer_Blacklist = new Lecturer_Blacklist(Integer.parseInt(lineSplit[0]), Integer.parseInt(lineSplit[1]), Integer.parseInt(lineSplit[2]));
                                this.allLecturer_Blacklists.add(currentLecturer_Blacklist);
                                break;
                            case "StudentGroup.txt":
                                StudentGroup currentStudentGroup = new StudentGroup(lineSplit[0]);
                                this.allStudentGroups.add(currentStudentGroup);
                                break;
                            case "StudentGroup_Blacklist.txt":
                                StudentGroup_Blacklist currentStudentGroup_Blacklist = new StudentGroup_Blacklist(Integer.parseInt(lineSplit[0]), Integer.parseInt(lineSplit[1]), lineSplit[2]);
                                this.allStudentGroup_Blacklist.add(currentStudentGroup_Blacklist);
                                break;
                        }
                    }
                    br.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("File: " + fileEntry.getName() + " not valid!");
            }
        }
    }

    public ArrayList<ClassroomAndTime> getAvailableClassroomAndTimes(){
        ArrayList<ClassroomAndTime> availableClassRoomAndTimes = new ArrayList<>();
        List<Classroom_Blacklist> findClassroom_Blacklist;
        boolean checkIfSlotinBlacklist=false;
        for (Classroom classroom : this.allClassrooms) {
            findClassroom_Blacklist = this.allClassroom_Blacklists.stream().filter(find -> find.getIdClassroom() == classroom.getIdClassroom()).collect(Collectors.toList());
            for (int i = 1; i <= 5; i++) { //loop for weekdays
                for (int j = 1; j <= 6; j++) { //each weekday has 6 TimeSlots
                    if (findClassroom_Blacklist.size() == 0) {
                        availableClassRoomAndTimes.add(new ClassroomAndTime(classroom.getIdClassroom(), classroom.getCapacity(), i, j));
                    } else {
                        checkIfSlotinBlacklist = false;
                        for (Classroom_Blacklist classroom_Blacklist : findClassroom_Blacklist) {
                            if (classroom_Blacklist.getIdDay() == i && classroom_Blacklist.getIdTimeSlot() == j) {
                                checkIfSlotinBlacklist = true;
                            }
                        }
                        if (!checkIfSlotinBlacklist) {
                            availableClassRoomAndTimes.add(new ClassroomAndTime(classroom.getIdClassroom(), classroom.getCapacity(), i, j));
                        }
                    }
                }
            }
        }
        return availableClassRoomAndTimes;
    }

    private static boolean isValidFile(String fileName) {
        if (fileName.equals("Classroom.txt") ||
                fileName.equals("Classroom_Blacklist.txt") ||
                fileName.equals("Lecture.txt") ||
                fileName.equals("Lecture_has_StudentGroup.txt") ||
                fileName.equals("Lecturer.txt") ||
                fileName.equals("Lecturer_Blacklist.txt") ||
                fileName.equals("StudentGroup.txt") ||
                fileName.equals("StudentGroup_Blacklist.txt")) {
            return true;
        } else {
            return false;
        }
    }
}
