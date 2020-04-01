package Backtracking;

import Data.Lecture;
import Data.Lecture_has_StudentGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Backtracking {

    private ArrayList<Lecture_has_StudentGroup> allLecture_has_StudentGroups;

    private ArrayList<Lecture> scheduledLectures;
    private ArrayList<Lecture> lecturesToSchedule;

    private ArrayList<Lecture> result;

    public Backtracking(ArrayList<Lecture> scheduledLectures, ArrayList<Lecture> lecturesToSchedule, ArrayList<Lecture_has_StudentGroup> allLecture_has_StudentGroups) {
        this.scheduledLectures = scheduledLectures;
        this.lecturesToSchedule = lecturesToSchedule;
        this.allLecture_has_StudentGroups = allLecture_has_StudentGroups;

        result = new ArrayList<>();
    }

    public ArrayList<Lecture> getResult() {
        return result;
    }

    //Wahlmöglichkeit noch einbauen, ob mit oder ohne ForwardChecking!
    public boolean startBacktracking() {
        if (lecturesToSchedule.size() < 1) {
            System.out.println("No lectures to schedule!");
            return false;
        } else {
            long timeStart = System.currentTimeMillis();
            boolean result = backtracking(this.scheduledLectures, this.lecturesToSchedule, 1);
            long timeEnd = System.currentTimeMillis();
            System.out.println("Zeit Backtracking: " + (timeEnd-timeStart) + " Millisek.");
            return result;
        }
    }

    private boolean backtracking(ArrayList<Lecture> scheduledLectures, ArrayList<Lecture> lecturesToSchedule, int depth) {

        ArrayList<Lecture> scheduledLecturesNextDepth;
        ArrayList<Lecture> lecturesToScheduleNextDepth;

        if (lecturesToSchedule.size() < 1) {
            return true;
        } else {
            Lecture currentLectureToSchedule = selectNextLecture(lecturesToSchedule);
            ArrayList<ClassroomAndTime> possibleDomain = currentLectureToSchedule.getApplicableTimeslots();
            for (ClassroomAndTime classroomAndTime : possibleDomain) {
                if (checkConsistency(scheduledLectures, currentLectureToSchedule, classroomAndTime)) {

                    currentLectureToSchedule.setScheduledClassroomAndTime(classroomAndTime);

                    scheduledLecturesNextDepth = new ArrayList<>(scheduledLectures);
                    scheduledLecturesNextDepth.add(currentLectureToSchedule);

                    lecturesToScheduleNextDepth = new ArrayList<>(lecturesToSchedule);
                    lecturesToScheduleNextDepth.remove(currentLectureToSchedule);

                    //ForwardChecking
                    lecturesToScheduleNextDepth = removeConflicts(lecturesToScheduleNextDepth, currentLectureToSchedule);

                    if (lecturesToScheduleNextDepth.size() < 1) {
                        this.result = scheduledLecturesNextDepth;
                    } else {
                        backtracking(scheduledLecturesNextDepth, lecturesToScheduleNextDepth, depth + 1);
                    }
                }
                if (this.result.size() > 0) {
                    System.out.println("Tiefe: " + depth);
                    return true;
                }
            }
        }
        return false;
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

    private ArrayList<Lecture> removeConflicts(ArrayList<Lecture> lecturesToScheduleNextDepth, Lecture scheduledLecture) {

        List<Lecture_has_StudentGroup> scheduledLecture_has_studentGroups = this.allLecture_has_StudentGroups.stream().filter(id -> id.getIdLecture() == scheduledLecture.getIdLecture()).collect(Collectors.toList());
        List<Lecture_has_StudentGroup> studentGroupsForLec;

        //Besitzt ein noch zu planendes Fach den zugewiesenen Zeitslot von scheduledLecture noch in den möglichen Timeslots, so wird dieser gelöscht (da bereits scheduledLecture zugewiesen).
        for (Lecture lec : lecturesToScheduleNextDepth) {
            if (lec.getApplicableTimeslots().contains(scheduledLecture.getScheduledClassroomAndTime())) {
                lec.getApplicableTimeslots().remove(scheduledLecture.getScheduledClassroomAndTime());
            }

            //Die Studentengruppen aus dem iterierenden Fach lec werden mit den Studentengruppen aus scheduledLecture verglichen...
            //Gleicht einer Studentengruppe aus dem Fach lec einer Studentengruppe aus dem verplanten scheduledLecture, so werden die zugewiesenen Zeitblöcke aus scheduledLec in den möglichen Zeitslots für lec entfernt...
            //Hierdurch kann eine Studentengruppe für einen gebuchten Zeitslot nicht nochmal gebucht werden.
            studentGroupsForLec = allLecture_has_StudentGroups.stream().filter(id -> id.getIdLecture() == lec.getIdLecture()).collect(Collectors.toList());
            for (Lecture_has_StudentGroup iterate1 : studentGroupsForLec) {
                for (Lecture_has_StudentGroup iterate2 : scheduledLecture_has_studentGroups) {
                    if (iterate1.getIdStudentGroup().equals(iterate2.getIdStudentGroup())) {
                        lec.getApplicableTimeslots().removeIf(classroomAndTime -> (classroomAndTime.getIdDay() == scheduledLecture.getScheduledClassroomAndTime().getIdDay() &&
                                classroomAndTime.getIdTimeSlot() == scheduledLecture.getScheduledClassroomAndTime().getIdTimeSlot()));
                    }
                }
            }

            //Unterrichtet der Dozent aus scheduledLecture auch andere Fächer, so wird er dort aus den Timeslots entfernt, da der Dozent schon für scheduledLec verplant ist.
            if (lec.getIdLecturer() == scheduledLecture.getIdLecturer()) {
                lec.getApplicableTimeslots().removeIf(classroomAndTime -> (classroomAndTime.getIdDay() == scheduledLecture.getScheduledClassroomAndTime().getIdDay() &&
                        classroomAndTime.getIdTimeSlot() == scheduledLecture.getScheduledClassroomAndTime().getIdTimeSlot()));
            }
        }
        return lecturesToScheduleNextDepth;
    }
}
