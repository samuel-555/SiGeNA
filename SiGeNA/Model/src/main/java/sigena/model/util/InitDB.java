package sigena.model.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.dao.UsuarioDAO;
import sigena.model.common.exception.DatabaseException;

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
                manutencao BOOLEAN NOT NULL,
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

    public void initFuncionarios() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS funcionarios (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nome VARCHAR(120) NOT NULL,
                cpf VARCHAR(20) NOT NULL UNIQUE,
                senha VARCHAR(100) NOT NULL,
                cargo VARCHAR(30) NOT NULL,
                area_atuacao VARCHAR(120) NOT NULL,
                turno ENUM('MANHA','TARDE','NOITE') NOT NULL DEFAULT 'MANHA',
                estado ENUM('ATIVO','FERIAS','LICENCA_MATERNIDADE','LICENCA_PATERNIDADE','AFASTADO') 
                    NOT NULL DEFAULT 'ATIVO',
                observacoes TEXT
            );
            """;
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }

        String insertExemplo = """
            INSERT INTO funcionarios (nome, cpf, senha, cargo, area_atuacao, turno, estado, observacoes)
            SELECT * FROM (SELECT 'Carlos Silva', '11111111122', '123', 'ZOOTECNISTA', 
                    'Alimentação', 'MANHA', 'ATIVO', 'Responsável pela alimentação dos herbívoros') AS tmp
            WHERE NOT EXISTS (SELECT 1 FROM funcionarios WHERE nome='Carlos Silva')
            UNION ALL
            SELECT * FROM (SELECT 'Mariana Souza', '22222222233', '123', 'TRATADOR', 
                    'Mamíferos', 'TARDE', 'ATIVO', 'Responsável pelos felinos') AS tmp2
            WHERE NOT EXISTS (SELECT 1 FROM funcionarios WHERE nome='Mariana Souza')
            UNION ALL
            SELECT * FROM (SELECT 'Roberto Lima', '33333333344', '123', 'VETERINARIO', 
                    'Saúde Animal', 'NOITE', 'FERIAS', 'Veterinário de plantão noturno') AS tmp3
            WHERE NOT EXISTS (SELECT 1 FROM funcionarios WHERE nome='Roberto Lima');
            """;
        try (Statement st = con.createStatement()) {
            st.executeUpdate(insertExemplo);
        }
    }

    public void initUsuarios() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id INT AUTO_INCREMENT PRIMARY KEY,
                cpf VARCHAR(20) NOT NULL UNIQUE,
                senha VARCHAR(100) NOT NULL,
                cargo VARCHAR(30) NOT NULL,
                funcionario_id INT,
                FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id)
                    ON DELETE CASCADE
            );
            """;
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }

        String insertAdmin = """
            INSERT INTO usuarios (cpf, senha, cargo)
            SELECT '11111111111', '123', 'GERENTE'
            WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE cpf='11111111111');
            """;
        try (Statement st = con.createStatement()) {
            st.executeUpdate(insertAdmin);
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
            
            initHabitats();
            initEspecies();
            
            
            initFuncionarios();
            initUsuarios();
            initAnimais();
            
            initPlanosAlimentares();

            new UsuarioDAO().sincronizarFuncionariosComUsuarios();

            initEspecies();
        } catch (SQLException | DatabaseException e) {
            throw new PersistenciaException("Erro ao inicializar tabelas: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws PersistenciaException {
        try {
            Connection con = ConexaoDB.getConnection();
            InitDB init = new InitDB(con);
            init.initTodos();
            System.out.println(" Banco de dados criado e sincronizado com sucesso!");
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao inicializar tabelas: " + e.getMessage());
        }
    }
}
