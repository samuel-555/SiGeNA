package sigena.model.dao;

import java.sql.*;

public class UsuarioDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/sistema_login";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    private String normalizarCPF(String cpf) {
        return cpf.replaceAll("[\\.\\-]", "").trim();
    }

    public boolean autenticar(String cpf, String senha) {
        cpf = normalizarCPF(cpf);
        senha = senha.trim();
        String sql = "SELECT * FROM usuarios WHERE REPLACE(REPLACE(cpf,'.',''),'-','') = ? AND senha = ?";
        boolean autenticado = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cpf);
            ps.setString(2, senha);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                autenticado = true;
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return autenticado;
    }

    public String cadastrar(String cpf, String senha) {
        cpf = normalizarCPF(cpf);
        senha = senha.trim();
        if (existeCPF(cpf)) {
            return "CPF já cadastrado! Não é permitido mais de um cadastro com o mesmo CPF.";
        }

        String sql = "INSERT INTO usuarios (cpf, senha) VALUES (?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cpf);
            ps.setString(2, senha);
            ps.executeUpdate();

            ps.close();
            conn.close();

            return "Usuário cadastrado com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao cadastrar o usuário.";
        }
    }

    private boolean existeCPF(String cpf) {
        cpf = normalizarCPF(cpf);
        String sql = "SELECT * FROM usuarios WHERE REPLACE(REPLACE(cpf,'.',''),'-','') = ?";
        boolean existe = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cpf);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                existe = true;
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return existe;
    }
}
