package sigena.model.util;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import sigena.model.common.exception.PersistenciaException;

public class InitDB {
    
    private final Connection con;
    
    public InitDB(Connection con){
        this.con = con;
    }
    
    
    public void initHabitats() throws SQLException{
         String sql = """
            CREATE TABLE IF NOT EXISTS habitat (
                nome VARCHAR(255) PRIMARY KEY,
                tipo VARCHAR(255) NOT NULL,
                capacidade INT NOT NULL,
                tamanho INT NOT NULL,
                precisaDeManutencao BOOLEAN NOT NULL,
                disponivel BOOLEAN NOT NULL
            );
            """;
         
         try(Statement st = con.createStatement()){
             st.executeUpdate(sql);
         }
    }
    public void initAnimais() throws SQLException{
         String sql = """
            CREATE TABLE animais (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nome VARCHAR(255),
                especie VARCHAR(255)
            );
            """;
         
         try(Statement st = con.createStatement()){
             st.executeUpdate(sql);
         }
    }
    
    public void initHabitat_animal() throws SQLException{
         String sql = """
            CREATE TABLE IF NOT EXISTS habitat_animal (
                    habitat_nome VARCHAR(100),
                    animal_id INT,
                    PRIMARY KEY (habitat_nome, animal_id),
                    FOREIGN KEY (habitat_nome) REFERENCES habitat(nome)
                     ON DELETE CASCADE,
                    FOREIGN KEY (animal_id) REFERENCES animais(id)
                     ON DELETE CASCADE
            );
            """;
         
         try(Statement st = con.createStatement()){
             st.executeUpdate(sql);
         }
    }
    
    public void initTodos() throws PersistenciaException{
        try{
            initHabitats();
            initAnimais();
            initHabitat_animal();
            
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao inicializar tabelas: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) throws PersistenciaException{
        try{
            Connection con = ConexaoDB.getConnection();
            InitDB init = new InitDB(con);
            init.initTodos();
            System.out.println("Tabelas criadas com sucesso");
        }
        catch(SQLException e){
            throw new PersistenciaException("erro ao inicializar tabelas: "+e.getMessage());
        }
    }
}
