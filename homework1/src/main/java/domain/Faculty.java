package domain;

public class Faculty {
    private String shortName;
    private Long facultyId;
    private String facultyName;

    public Faculty(String shortName, Long facultyId, String facultyName) {
        this.shortName = shortName;
        this.facultyId = facultyId;
        this.facultyName = facultyName;
    }

    public String getShortName() {
        return shortName;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }
}
