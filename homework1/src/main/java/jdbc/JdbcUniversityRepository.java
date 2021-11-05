package jdbc;

import domain.Faculty;
import domain.University;
import exception.UniversityException;
import repository.FacultyRepository;
import repository.UniversityRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.System.err;
import static java.lang.System.out;

public class JdbcUniversityRepository implements UniversityRepository, FacultyRepository {
    public final DatabaseService dbService;

    public JdbcUniversityRepository(DatabaseService dbService) {
        this.dbService = dbService;
    }

    @Override
    public List<University> findAll(){
        try (Connection connection = dbService.openConnection()){
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("select id, name, short_name as shortName, foundation_year AS fYear from university");

            return retrieveFromResultSet(resultSet);

        } catch (SQLException exc) {
            out.println("Couldn't get all university because of an error: " + exc.getMessage());
            return Collections.emptyList(); //чтобы не падал проект на исключение. Возвращаем пустой лист
        }
    }

    @Override
    public List<University> findById(Long universityId){
        try (Connection connection = dbService.openConnection()){
            PreparedStatement stmt = connection.prepareStatement("select id, name, short_name as shortName, foundation_year AS fYear from university where id = ?");
            stmt.setLong(1, universityId);
            ResultSet resultSet = stmt.executeQuery();

            return retrieveFromResultSet(resultSet);

        } catch (SQLException exc) {
            out.println("Couldn't get all university because of an error: " + exc.getMessage());
            return Collections.emptyList(); //чтобы не падал проект на исключение. Возвращаем пустой лист
        }
    }

    public List<Faculty> findFacultyById(Long universityId){
        try (Connection connection = dbService.openConnection()){
            PreparedStatement stmt = connection.prepareStatement("\n" +
                    "select short_name as shortName, f.id AS facultyId, f.name AS facultyName from university as u join public.faculties as f ON u.id = f.university_id where u.id = ?");
            stmt.setLong(1, universityId);
            ResultSet resultSet = stmt.executeQuery();

           return retrieveFacultyFromResultSet(resultSet);

        } catch (SQLException exc) {
            out.println("Couldn't get all university because of an error: " + exc.getMessage());
            return Collections.emptyList(); //чтобы не падал проект на исключение. Возвращаем пустой лист
        }
    }

    public List<University> findAllFacultyCnt(){
        try (Connection connection = dbService.openConnection()){
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("\n" +
                    "select short_name as shortName, COUNT(f.university_id ) AS facultyCnt from university as u left join public.faculties as f ON u.id = f.university_id group by short_name\n" +
                    "\n");

            return retrieveCntFacultyFromResultSet(resultSet);

        } catch (SQLException exc) {
            out.println("Couldn't get all university because of an error: " + exc.getMessage());
            return Collections.emptyList(); //чтобы не падал проект на исключение. Возвращаем пустой лист
        }
    }

    private List<University> retrieveFromResultSet(ResultSet resultSet) throws SQLException {
        List<University> universities = new ArrayList<>();

            while (resultSet.next()) {
                Long universityId = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String shortName = resultSet.getString("shortName");
                Integer fYear = resultSet.getInt("fYear");

                University university = new University(universityId, name, shortName, fYear);
                universities.add(university);
            }

        return universities;
    }

    private List<Faculty> retrieveFacultyFromResultSet(ResultSet resultSet) throws SQLException {
        List<Faculty> faculties = new ArrayList<>();

            while (resultSet.next()) {

                String shortName = resultSet.getString("shortName");
                Long facultyId = resultSet.getLong("facultyId");
                String facultyName = resultSet.getString("facultyName");

                Faculty faculty = new Faculty(shortName, facultyId, facultyName);
                faculties.add(faculty);
            }

        return faculties;
    }

    private List<University> retrieveCntFacultyFromResultSet(ResultSet resultSet) throws SQLException {
        List<University> universities = new ArrayList<>();

        while (resultSet.next()) {
            String shortName = resultSet.getString("shortName");
            Integer facultyCnt = resultSet.getInt("facultyCnt");

            University university = new University( shortName, facultyCnt);
            universities.add(university);
        }

        return universities;
    }

    /*******************************Изменение данных***************************************/
    /**Вставка факультета**/

    public Faculty addFaculty(String facultyName, Long universityId){
        try(Connection connection = dbService.openConnection()){
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO public.faculties(\n" +
                    "name, university_id\n" +
                    ")\n" +
                    "values(?, ?)");
            stmt.setString(1, facultyName);
            stmt.setLong(2, universityId);
            stmt.executeUpdate();
            out.println("Line add");
        } catch (SQLException exc){
            out.println("Line didn't add");
            throw new RuntimeException(exc);
        }
        return null;
    }

    /**Удаление существующего университета по ID **/
    public University deleteUniversity(Long universityId){
        try(Connection connection = dbService.openConnection()) {
            PreparedStatement stmt = connection.prepareStatement("delete from university where id = ?");
            //String u = Long.toString(universityId);
            stmt.setLong(1, universityId);
            stmt.executeUpdate();

        } catch (SQLException exc){

        }

        return null;
    }
    /**Изменение существующего университета**/
    public University updateUniversity(Long universityId, String shortName,String name, Integer fYear){
        try(Connection connection = dbService.openConnection()) {
            PreparedStatement stmt = connection.prepareStatement("update university \n" +
                    "set\n" +
                    "\t name = ?\n" +
                    "\t,short_name = ?\n" +
                    "\t,foundation_YeaR = ?\n" +
                    "where id =  ?");

            stmt.setString(1, name);
            stmt.setString(2, shortName);
            stmt.setInt(3, fYear);
            stmt.setLong(4, universityId);
            int createdRows = stmt.executeUpdate();
            if(createdRows == 0) {
                err.println("universityId not found");
                throw new UniversityException();
            }

        } catch (SQLException | UniversityException exc){
            out.println("Line cannot update");
            throw new RuntimeException(exc);
        }

        return null;
    }

}
