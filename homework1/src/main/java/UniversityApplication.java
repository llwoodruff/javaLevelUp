import domain.Faculty;
import domain.University;
import jdbc.DatabaseService;
import jdbc.JdbcUniversityRepository;
import repository.FacultyRepository;
import repository.UniversityRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UniversityApplication {
    public static void main(String[] args) throws SQLException {

        DatabaseService dbService = new DatabaseService();
        Connection connection = dbService.openConnection();
        System.out.println("Connection has been opened");

        UniversityRepository universityRepository = new JdbcUniversityRepository(dbService);

       /* List<University> universities = universityRepository.findAll();
        for(University u : universities){
            System.out.println(u.getUniversityId() + " '" + u.getName() + "' " + u.getShortName() + " " + u.getFoundationYear());
        }*/

        System.out.println("\n    Вывод университета по ИД ");
        List<University> university = universityRepository.findById(2L);
        for(University u : university){
            System.out.println("Вывод " + u.getUniversityId() + " '" + u.getName() + "' " + u.getShortName() + " " + u.getFoundationYear());
        }
        System.out.println("\n\n");


        System.out.println("\n    Вывод факультетов по ИД университета ");
        FacultyRepository facultyRepository = new JdbcUniversityRepository(dbService);
        List<Faculty> faculty = facultyRepository.findFacultyById(2L);
        for(Faculty f : faculty) {
            System.out.println(f.getShortName() + " " + f.getFacultyId() + " " + f.getFacultyName());
        }
        System.out.println("\n\n");


        System.out.println("\n    Вывод количества факультетов университетов ");
        List<University> universityFacultyCount = universityRepository.findAllFacultyCnt();
        for(University u : universityFacultyCount) {
            System.out.println(u.getShortName() + " " + u.getFacultyCnt());
        }
        System.out.println("\n\n");

        /**Добавление факультета**/
        String facultyName = "Факультет романо-германской филологии2";
        Long universityId = 12L;
        //facultyRepository.addFaculty(facultyName, universityId);


        /**УДаление университета**/
        universityId = 12L;
        //universityRepository.deleteUniversity(universityId);

        /**Обновление данных об университете**/
        universityId = 12L;
        String name = "Российский экономический УНИВЕРСИТЕТ имени Г.В. Плеханова";
        String shortName = "РЭУ";
        Integer fYear = 1901;

        universityRepository.updateUniversity(universityId, shortName, name, fYear);



        connection.close();
        System.out.println("Connection closed");
    }
}
