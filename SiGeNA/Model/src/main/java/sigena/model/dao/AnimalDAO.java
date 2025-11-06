package sigena.model.dao;

import java.sql.*;
import sigena.model.domain.Animal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AnimalDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_login";
    private static final String USUARIO = "root";
    private static final String SENHA = "";
    
    public static void cadastrar(Animal animal) {
        String dataCadastro;
        LocalDateTime dataEHora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        dataCadastro = dataEHora.format(formato);
    }
}
