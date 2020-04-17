package Backtracking;

import Data.Lecture;
import Data.Lecture_has_StudentGroup;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Backtracking {

    private ArrayList<Lecture_has_StudentGroup> allLecture_has_StudentGroups;
    private ArrayList<Lecture> scheduledLectures;
    private ArrayList<Lecture> lecturesToSchedule;

    private ArrayList<Lecture> result;
    public ArrayList<Lecture> getResult() {
        return result;
    }

    public Backtracking(ArrayList<Lecture> scheduledLectures, ArrayList<Lecture> lecturesToSchedule, ArrayList<Lecture_has_StudentGroup> allLecture_has_StudentGroups) {
        this.scheduledLectures = scheduledLectures;
        this.lecturesToSchedule = lecturesToSchedule;
        this.allLecture_has_StudentGroups = allLecture_has_StudentGroups;

        result = new ArrayList<>();
    }

    //forwardCheck == true -> with forwardCheck; false -> only backtracking
    public boolean startBacktracking(boolean forwardCheck){
        if (lecturesToSchedule.size() < 1) {
            System.out.println("No lectures to schedule!");
            return false;
        } else {
            long timeStart = System.currentTimeMillis();
            boolean result = backtracking(this.scheduledLectures, this.lecturesToSchedule, 1, forwardCheck);
            long timeEnd = System.currentTimeMillis();
            //System.out.println("Zeit Backtracking: " + (timeEnd - timeStart) + " Millisek.");
            return result;
        }
    }

    public void writeResultsToCSV() throws IOException {
        if (this.result.size() < 1) {
            System.out.println("No results to create a .csv-File!");
        } else {
            FileWriter csvWriter = new FileWriter("result.csv");
            csvWriter.append("idRaum" + ";");
            csvWriter.append("idDay" + ";");
            csvWriter.append("idTimeSlot" + ";");
            csvWriter.append("idPlanObjectLecture" + "\n");

            for (Lecture lec:this.result) {
                csvWriter.append(lec.getScheduledClassroomAndTime().getIdClassroom() + ";");
                csvWriter.append(lec.getScheduledClassroomAndTime().getIdDay() + ";");
                csvWriter.append(lec.getScheduledClassroomAndTime().getIdTimeSlot() +";");
                csvWriter.append(lec.getIdLectureObject() + "\n");
            }
            System.out.println("Die Ergebnisse befinden sich in result.csv im Ausführungsverzeichnis");
            csvWriter.flush();
            csvWriter.close();
        }
    }

    private boolean backtracking(ArrayList<Lecture> scheduledLectures, ArrayList<Lecture> lecturesToSchedule, int depth, boolean forwardCheck) {
        List<ClassroomAndTime> all = getAllTimeslots(lecturesToSchedule);
        ArrayList<Lecture> scheduledLecturesNextDepth;
        ArrayList<Lecture> lecturesToScheduleNextDepth = null;
        List<ClassroomAndTime> deletedTimeslotsInCurrentDepth = null;

        boolean result;
        System.out.println("Rekursionsebene: " + depth);

        if (lecturesToSchedule.size() < 1) {
            result = true;
        } else {
            Lecture currentLectureToSchedule = selectNextLecture(lecturesToSchedule);
            ArrayList<ClassroomAndTime> possibleDomain = currentLectureToSchedule.getApplicableTimeslots();
            result = false;

            for (ClassroomAndTime classroomAndTime : possibleDomain) {
                if (checkConsistency(scheduledLectures, currentLectureToSchedule, classroomAndTime)) {

                    scheduledLecturesNextDepth = new ArrayList<>(scheduledLectures);
                    currentLectureToSchedule.setScheduledClassroomAndTime(classroomAndTime);
                    scheduledLecturesNextDepth.add(currentLectureToSchedule);

                    lecturesToScheduleNextDepth = new ArrayList<>(lecturesToSchedule);
                    lecturesToScheduleNextDepth.remove(currentLectureToSchedule);

                    if(forwardCheck){
                        removeConflicts(lecturesToScheduleNextDepth, currentLectureToSchedule);
                        deletedTimeslotsInCurrentDepth = getAllDeletedTimeslots(lecturesToScheduleNextDepth);
                    }
                    if (lecturesToScheduleNextDepth.size() < 1) {
                        this.result = scheduledLecturesNextDepth;
                        result = true;
                    } else {
                        result = backtracking(scheduledLecturesNextDepth, lecturesToScheduleNextDepth, depth + 1, forwardCheck);
                    }
                }
                if (result == true) {
                    break;
                } if(forwardCheck) {
                    //Beim Zurückspringen und fehlender Ergebnisfindung werden die Timeslots für die aktuelle Ebene wieder hergestellt
                    restoreTimeslotsAfterForwardCheck(lecturesToScheduleNextDepth, currentLectureToSchedule, deletedTimeslotsInCurrentDepth);
                }
            }
        }
        return result;
    }

    private Lecture selectNextLecture(ArrayList<Lecture> lectures) {
        int min = 0;
        Lecture currentMin = null;
        for (Lecture lec : lectures) {
            if (lec.getApplicableTimeslots().size() < min || min == 0) {
                currentMin = lec;
                min = currentMin.getApplicableTimeslots().size();
            }
        }
        return currentMin;
    }

    private boolean checkConsistency(ArrayList<Lecture> scheduledLecs, Lecture currentLecToSchedule, ClassroomAndTime currentClassroomAndTime) {

        List<Lecture_has_StudentGroup> studentGroupsForCurrentLecToSchedule = this.allLecture_has_StudentGroups.stream().filter(id -> id.getIdLecture() == currentLecToSchedule.getIdLecture()).collect(Collectors.toList());
        List<Lecture_has_StudentGroup> studentGroupsForScheduledLec;

        for (Lecture scheduledLec : scheduledLecs) {
            if (scheduledLec.getScheduledClassroomAndTime().equals(currentClassroomAndTime)) {
                return false;
            }

            //wenn ein Dozent schon am selben Tag + Slot eine andere Vorlesung hält
            if (scheduledLec.getIdLecturer() == currentLecToSchedule.getIdLecturer() &&
                    scheduledLec.getScheduledClassroomAndTime().getIdTimeSlot() == currentClassroomAndTime.getIdTimeSlot() &&
                    scheduledLec.getScheduledClassroomAndTime().getIdDay() == currentClassroomAndTime.getIdDay()) {
                return false;
            }
            //wenn eine Studentgroup schon am selben Tag + Slot eine andere Vorlesung hat
            studentGroupsForScheduledLec = this.allLecture_has_StudentGroups.stream().filter(id -> id.getIdLecture() == scheduledLec.getIdLecture()).collect(Collectors.toList());
            for (Lecture_has_StudentGroup studentGroupForScheduledLec : studentGroupsForScheduledLec) {
                for (Lecture_has_StudentGroup studentGroupForCurrentLecToSchedule : studentGroupsForCurrentLecToSchedule) {
                    if (studentGroupForScheduledLec.getIdStudentGroup().equals(studentGroupForCurrentLecToSchedule.getIdStudentGroup()) &&
                            scheduledLec.getScheduledClassroomAndTime().getIdDay() == currentClassroomAndTime.getIdDay() &&
                            scheduledLec.getScheduledClassroomAndTime().getIdTimeSlot() == currentClassroomAndTime.getIdTimeSlot()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void removeConflicts(ArrayList<Lecture> lecturesToScheduleNextDepth, Lecture scheduledLecture) {

        List<Lecture_has_StudentGroup> scheduledLecture_has_studentGroups = this.allLecture_has_StudentGroups.stream().filter(id -> id.getIdLecture() == scheduledLecture.getIdLecture()).collect(Collectors.toList());
        ClassroomAndTime scheduledClassroomAndTime = scheduledLecture.getScheduledClassroomAndTime();

        List<Lecture_has_StudentGroup> studentGroupsForLec;

        for (Lecture lec : lecturesToScheduleNextDepth) {
            lec.getDeletedTimeslotsInFC().clear();
            ArrayList<ClassroomAndTime> possibleSlots = new ArrayList<ClassroomAndTime>(lec.getApplicableTimeslots());
            studentGroupsForLec = allLecture_has_StudentGroups.stream().filter(id -> id.getIdLecture() == lec.getIdLecture()).collect(Collectors.toList());

            //Besitzt ein noch zu planendes Fach den zugewiesenen Zeitslot von scheduledLecture in den möglichen Timeslots, so wird dieser gelöscht (da bereits scheduledLecture zugewiesen).
            for (ClassroomAndTime slot : possibleSlots) {
                if (slot.equals(scheduledClassroomAndTime)) {
                    lec.getApplicableTimeslots().remove(slot);
                    if (!(lec.getDeletedTimeslotsInFC().contains(slot))) {
                        lec.getDeletedTimeslotsInFC().add(slot);
                    }
                }
                //Unterrichtet der Dozent aus scheduledLecture auch andere Fächer, so wird er dort aus den Timeslots entfernt, da der Dozent schon für scheduledLec verplant ist.
                if (lec.getIdLecturer() == scheduledLecture.getIdLecturer()) {
                    if (slot.getIdDay() == scheduledClassroomAndTime.getIdDay() && slot.getIdTimeSlot() == scheduledClassroomAndTime.getIdTimeSlot()) {
                        lec.getApplicableTimeslots().remove(slot);
                        if (!(lec.getDeletedTimeslotsInFC().contains(slot))) {
                            lec.getDeletedTimeslotsInFC().add(slot);
                        }
                    }
                }
                for (Lecture_has_StudentGroup iterate1 : studentGroupsForLec) {
                    for (Lecture_has_StudentGroup iterate2 : scheduledLecture_has_studentGroups) {
                        if (iterate1.getIdStudentGroup().equals(iterate2.getIdStudentGroup()) &&
                                slot.getIdDay() == scheduledClassroomAndTime.getIdDay() && slot.getIdTimeSlot() == scheduledClassroomAndTime.getIdTimeSlot()) {
                            lec.getApplicableTimeslots().remove(slot);
                            if (!(lec.getDeletedTimeslotsInFC().contains(slot))) {
                                lec.getDeletedTimeslotsInFC().add(slot);
                            }
                        }
                    }
                }
            }
        }
    }

    private ArrayList<ClassroomAndTime> getAllDeletedTimeslots(ArrayList<Lecture> lectures) {
        ArrayList<ClassroomAndTime> allTimeslots = new ArrayList<ClassroomAndTime>();
        for (Lecture lec : lectures) {
            int index = lec.getIdLectureObject();
            for (ClassroomAndTime slot : lec.getDeletedTimeslotsInFC()) {
                allTimeslots.add(new ClassroomAndTime(index, slot.getIdClassroom(), slot.getIdDay(), slot.getIdTimeSlot()));
            }
        }
        return allTimeslots;
    }

    private void restoreTimeslotsAfterForwardCheck(ArrayList<Lecture> lectures, Lecture currentLec, List<ClassroomAndTime> deleted) {
        currentLec.getDeletedTimeslotsInFC().clear();
        currentLec.setScheduledClassroomAndTime(null);
        for (Lecture l : lectures) {
            for (ClassroomAndTime slot : deleted) {
                if (l.getIdLectureObject() == slot.getIndexLecObject()) {
                    l.getApplicableTimeslots().add(slot);
                }
            }
        }
        lectures.add(currentLec);
    }

    //Method for Debugging
    private ArrayList<ClassroomAndTime> getAllTimeslots(ArrayList<Lecture> lectures) {
        ArrayList<ClassroomAndTime> allTimeslots = new ArrayList<ClassroomAndTime>();
        for (Lecture lec : lectures) {
            for (ClassroomAndTime slot : lec.getApplicableTimeslots()) {
                slot.setIndexLecObject(lec.getIdLectureObject());
                allTimeslots.add(slot);
            }
        }
        return allTimeslots;
    }
}
