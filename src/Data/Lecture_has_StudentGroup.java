package Data;

public class Lecture_has_StudentGroup {

    private int idLecture;
    private String idStudentGroup;
    private int numParticipants;

    public Lecture_has_StudentGroup(int idLecture, String idStudentGroup, int numParticipants) {
        this.idLecture = idLecture;
        this.idStudentGroup = idStudentGroup;
        this.numParticipants = numParticipants;
    }

    public int getIdLecture() {
        return idLecture;
    }

    public int getNumParticipants() {
        return numParticipants;
    }

    public String getIdStudentGroup() {
        return idStudentGroup;
    }
}
