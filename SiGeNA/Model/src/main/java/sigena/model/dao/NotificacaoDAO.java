package sigena.model.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Notificacao;
import sigena.model.domain.Usuario;
import sigena.model.util.ConexaoDB;

public class NotificacaoDAO {

    public void salvar(Notificacao n) {
        String sql = "INSERT INTO notificacao(idDestinatario, titulo, lida, "
                + "data_criacao) values (?, ?, ?, NOW())";

        try {
            Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, n.getIdDestinatario());
            ps.setString(2, n.getTitulo());
            ps.setBoolean(3, false);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void criarParaTodos(Notificacao n) throws PersistenciaException {

        UsuarioDAO usuarioDao = new UsuarioDAO();
        List<Usuario> usuarios = usuarioDao.listarUsuarios();

        for (Usuario u : usuarios) {
            n.setIdDestinatario(u.getId());
            salvar(n);
        }
    }

    public List<Notificacao> listarPorUsuario(int idDestinatario) {
        String sql = "SELECT * FROM notificacao WHERE idDestinatario = ? "
                + "ORDER BY data_criacao DESC";
        List<Notificacao> lista = new ArrayList();
        try {
            Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idDestinatario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong("id");
                String titulo = rs.getString("titulo");
                LocalDateTime data = rs.getTimestamp("data_criacao").toLocalDateTime();
                Notificacao n = new Notificacao(id, titulo, idDestinatario, data);
                lista.add(n);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao exibir notificacoes: " + e.getMessage());
        }
        return lista;

    }

    public void marcarComoLida(Notificacao n) {

        String sql = "UPDATE notificacao SET lida = true WHERE id = ? AND id_usuario = ?";

        try {
            Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, n.getId());
            ps.setInt(2, n.getIdDestinatario());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Notificacao buscarPorId(Long id) {
        String sql = "SELECT * FROM notificacao WHERE id = ?";
        try {
            Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            ps.setLong(1, id);
            
            if(rs.next()){
                String titulo = rs.getString("titulo");
                LocalDateTime data = rs.getTimestamp("data").toLocalDateTime();
                int idDestinatario = rs.getInt("idDestinatario");
                Notificacao n = new Notificacao(id, titulo, idDestinatario, data);
                return n;
            }
            
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
