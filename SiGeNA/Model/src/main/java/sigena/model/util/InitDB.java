package sigena.model.util;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import sigena.model.common.exception.PersistenciaException;

public class InitDB {

    private final Connection con;

    public InitDB(Connection con) {
        this.con = con;
    }

    public void initHabitats() throws SQLException {
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

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initAnimais() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS animais (
                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                  nome VARCHAR(100) NOT NULL, 
                  id_especie INT NOT NULL,
                  sexo VARCHAR(20) NOT NULL,
                  data_de_nascimento DATE NOT NULL,
                  peso DOUBLE NOT NULL,
                  hostil BOOLEAN NOT NULL,
                  data_de_insercao DATETIME NOT NULL,
                  FOREIGN KEY (id_especie) REFERENCES especie(id)
                     ON UPDATE CASCADE
            );
            """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initPlanosAlimentares() throws SQLException {
        String planosSql = """
            CREATE TABLE IF NOT EXISTS planos_alimentares (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                animal_id BIGINT NOT NULL,
                data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (animal_id) REFERENCES animais(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE
            );
            """;

        String itensSql = """
            CREATE TABLE IF NOT EXISTS itens_plano_alimentar (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                plano_id BIGINT NOT NULL,
                alimento VARCHAR(255) NOT NULL,
                gramatura DOUBLE,
                vezes_por_dia INT,
                FOREIGN KEY (plano_id) REFERENCES planos_alimentares(id)
                    ON DELETE CASCADE
            );
            """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(planosSql);
            st.executeUpdate(itensSql);
        }
    }

    public void initHabitat_animal() throws SQLException {
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

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initUsuarios() throws SQLException {
        String sql = """
        CREATE TABLE IF NOT EXISTS usuarios (
            id INT AUTO_INCREMENT PRIMARY KEY,
            cpf VARCHAR(20) UNIQUE NOT NULL,
            senha VARCHAR(255) NOT NULL
        );
    """;
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
        String sqlInsert = """
            INSERT INTO usuarios (id, cpf, senha)
            SELECT 1, '12345678900', '1234'
            WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE id = 1);
        """;

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sqlInsert);
        }
    }

    public void initEspecies() throws SQLException {
        String sql = """
        CREATE TABLE IF NOT EXISTS especie (
            id INT AUTO_INCREMENT PRIMARY KEY,
            nome VARCHAR(255) NOT NULL,
            classe VARCHAR(255),
            habitat VARCHAR(255) NOT NULL,
            alimentacao VARCHAR(255) NOT NULL,
            predador BOOLEAN NOT NULL,
            observacoes TEXT
        );
    """;
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initTodos() throws PersistenciaException {
        try {
            //initHabitats();
            initEspecies();
            initAnimais();
            initPlanosAlimentares();
            //initHabitat_animal();
            initUsuarios();
            

        } catch (SQLException e) {
            throw new PersistenciaException("erro ao inicializar tabelas: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws PersistenciaException {
        try {
            Connection con = ConexaoDB.getConnection();
            InitDB init = new InitDB(con);
            init.initTodos();
            System.out.println("Tabelas criadas com sucesso");
        } catch (SQLException e) {
            throw new PersistenciaException("erro ao inicializar tabelas: " + e.getMessage());
        }
    }
}
