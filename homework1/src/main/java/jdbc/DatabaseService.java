package jdbc;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService {
    public Connection openConnection(){
        try{
            return DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/university",
                    "postgres" ,
                    "1234"
            );
        } catch (SQLException exc){
            System.out.println("Couldn't connect to database");
            throw  new RuntimeException(exc);
        }
    }
}
