package repository;

import domain.Faculty;

import java.util.List;

public interface FacultyRepository {
    List<Faculty> findFacultyById(Long universityId);
    Faculty addFaculty(String facultyName, Long universityId);
}
