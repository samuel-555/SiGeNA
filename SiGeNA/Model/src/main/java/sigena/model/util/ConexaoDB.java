package sigena.model.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoDB {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "sigena";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC não encontrado!", e);
        }

        try (Connection con = DriverManager.getConnection(URL, USUARIO, SENHA);
             Statement statement = con.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        }

        return DriverManager.getConnection(URL + DB_NAME, USUARIO, SENHA);
    }
}