package sigena.model.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class ConexaoDB {
    
    private static final String url = "jdbc:mysql://localhost:3306/";
    private static final String db_name = "sigena";
    private static final String user = "root";
    private static final String password = "";
    
    public static Connection getConnection() throws SQLException{
        try(Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement()){
            
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + db_name);
        }
        
        Connection connection = DriverManager.getConnection(url + db_name, user, password);
        return connection;
        
    }  
}