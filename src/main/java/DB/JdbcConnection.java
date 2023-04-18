package DB;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;


public class JdbcConnection {
    public static Connection connection;

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fileHider?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false", "root", "root");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Connection Established !!!");
        return connection;
    }

    ;

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



}
