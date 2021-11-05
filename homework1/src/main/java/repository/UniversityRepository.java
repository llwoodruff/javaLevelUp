package repository;

import domain.University;

import java.util.List;

public interface UniversityRepository {
    List<University> findAll();
    List<University> findById(Long universityId);
    List<University> findAllFacultyCnt();
    University deleteUniversity(Long universityId);
    University updateUniversity(Long universityId, String shortName,String name, Integer fYear);
}
