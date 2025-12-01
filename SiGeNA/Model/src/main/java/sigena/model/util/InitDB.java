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
              id BIGINT AUTO_INCREMENT PRIMARY KEY,
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
                animal_id BIGINT,
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

    public void initTratamento() throws SQLException {
        String sql = """
                     
                CREATE TABLE IF NOT EXISTS tratamento(
                     id INT AUTO_INCREMENT PRIMARY KEY,
                     animal_id BIGINT NOT NULL,
                     vet_id INT NOT NULL,
                     diagnostico TEXT NOT NULL,
                     medicacao TEXT NOT NULL,
                     frequencia INT,
                     observacao LONGTEXT,
                     tipo TEXT NOT NULL,
                     status TEXT NOT NULL,
                     data_inicio DATETIME NOT NULL,
                     data_final DATE NOT NULL,
                     horario TIME
                     
                    
                );
                """;
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

public void initDoacoes() throws SQLException {
    String sql = """
        CREATE TABLE IF NOT EXISTS doacoes (
            id BIGINT PRIMARY KEY AUTO_INCREMENT,
            nome_doador VARCHAR(150) NOT NULL,
            tipo VARCHAR(50) NOT NULL,
            valor_monetario DECIMAL(10,2),
            descricao_outro VARCHAR(255),
            observacoes TEXT,
            status VARCHAR(20) NOT NULL DEFAULT 'ATIVA',
            recibo_emitido BOOLEAN DEFAULT FALSE,
            data_doacao DATE NOT NULL,
            data_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        );
    """;

    try (Statement stmt = con.createStatement()) {
        stmt.execute(sql);
    }
}


    public void initRecibosDoacao() throws SQLException {
    String sql = """
        CREATE TABLE IF NOT EXISTS recibo_doacao (
            id BIGINT PRIMARY KEY AUTO_INCREMENT,
            doacao_id BIGINT NOT NULL,
            data_emissao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (doacao_id) REFERENCES doacoes(id)
                ON DELETE CASCADE
        );
        """;

    try (Statement st = con.createStatement()) {
        st.executeUpdate(sql);
    }
}


    public void initProdutos() throws SQLException {
        String sql = """ 
                     CREATE TABLE IF NOT EXISTS produtos(
                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                     fornecedor_id BIGINT NOT NULL,
                     quantidade INT NOT NULL,
                     nome VARCHAR(255) NOT NULL,
                     tipo VARCHAR(100) NOT NULL,
                     lote DATE,
                     validade DATE,
                     disponivel BOOLEAN NOT NULL
                     );
                     """;
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initFornecedores() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS fornecedores (
                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                  nome VARCHAR(100) NOT NULL, 
                  telefone VARCHAR(20),
                  email VARCHAR(50),
                  endereco VARCHAR(100),
                  tipo VARCHAR(50) NOT NULL,
                  descricao TEXT
            );
            """;
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initEnriquecimentos() throws SQLException {
        String sql = """
        CREATE TABLE IF NOT EXISTS enriquecimento (
            id INT AUTO_INCREMENT PRIMARY KEY,
            nome VARCHAR(255) NOT NULL,
            tipo VARCHAR(255) NOT NULL,
            especie_destinada VARCHAR(255),
            frequencia VARCHAR(100),
            observacoes TEXT,
            data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        );
        """;
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public void initEnriquecimento_habitat() throws SQLException {
        String sql = """
        CREATE TABLE IF NOT EXISTS enriquecimento_habitat (
            enriquecimento_id INT NOT NULL,
            habitat_nome VARCHAR(255) NOT NULL,
            PRIMARY KEY (enriquecimento_id, habitat_nome),
            CONSTRAINT fk_enriq FOREIGN KEY (enriquecimento_id)
                REFERENCES enriquecimento(id) ON DELETE CASCADE,
            CONSTRAINT fk_hab FOREIGN KEY (habitat_nome)
                REFERENCES habitat(nome) ON DELETE CASCADE
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
            initTratamento();
            initPlanosAlimentares();
            initEnriquecimentos();
            initEnriquecimento_habitat();
            initHabitat_animal();
            initDoacoes();
            initRecibosDoacao();

            new UsuarioDAO().sincronizarFuncionariosComUsuarios();

            initFornecedores();
            initProdutos();
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
