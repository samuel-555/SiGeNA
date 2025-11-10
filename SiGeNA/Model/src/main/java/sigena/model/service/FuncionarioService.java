package sigena.model.service;

import sigena.model.dao.FuncionarioDAO;
import sigena.model.dao.UsuarioDAO;
import sigena.model.domain.Funcionario;
import sigena.model.common.exception.BusinessException;
import sigena.model.common.exception.DatabaseException;
import sigena.model.common.exception.ValidationException;
import java.sql.SQLException;

public class FuncionarioService {

    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void salvar(Funcionario f) throws ValidationException, DatabaseException, SQLException {
        validar(f);
        funcionarioDAO.salvar(f);

        usuarioDAO.criarUsuario(f);
    }

    public void atualizar(Funcionario f) throws ValidationException, DatabaseException, SQLException {
        validar(f);
        funcionarioDAO.atualizar(f);
        usuarioDAO.atualizarSenhaOuCargo(f);
    }

    public void deletar(int id, String setor, String turno) throws BusinessException, DatabaseException, SQLException {
        int restantes = funcionarioDAO.contarPorSetorETurnoExceto(setor, turno, id);
        if (restantes == 0) {
            throw new ValidationException("deve haver pelo menos um funcionário por setor/turno.");
        }
        funcionarioDAO.deletar(id);
        usuarioDAO.deletarPorFuncionario(id);
    }

    public Funcionario buscarPorId(int id) throws SQLException, DatabaseException {
        return funcionarioDAO.buscarPorId(id);
    }

    public java.util.List<Funcionario> listar() throws SQLException, DatabaseException {
        return funcionarioDAO.listar();
    }

    private void validar(Funcionario f) throws ValidationException {
        if (f.getNome() == null || f.getNome().isBlank()) {
            throw new ValidationException("O nome do funcionário é obrigatório.");
        }
        if (f.getSenha() == null || f.getSenha().isBlank()) {
            throw new ValidationException("A senha do funcionário é obrigatória.");
        }
    }
}