package sigena.model.service;

import sigena.model.dao.UsuarioDAO;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.common.exception.DatabaseException;
import sigena.model.domain.Funcionario;
import sigena.model.domain.Usuario;

public class GestaoUsuarioService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario autenticar(String cpf, String senha) throws PersistenciaException {
        if (cpf == null || senha == null || cpf.isBlank() || senha.isBlank()) {
            throw new PersistenciaException("CPF e senha são obrigatórios!");
        }
        return usuarioDAO.autenticar(cpf, senha);
    }

    public void criarUsuario(Funcionario funcionario) throws DatabaseException, PersistenciaException {
        if (funcionario == null) {
            throw new PersistenciaException("Funcionário não informado.");
        }
        if (funcionario.getCpf() == null || funcionario.getCpf().isBlank()) {
            throw new PersistenciaException("CPF do funcionário é obrigatório.");
        }
        if (funcionario.getSenha() == null || funcionario.getSenha().isBlank()) {
            throw new PersistenciaException("Senha do funcionário é obrigatória.");
        }
        if (usuarioDAO.existeCPF(funcionario.getCpf())) {
            throw new PersistenciaException("Já existe um usuário cadastrado com este CPF.");
        }
        usuarioDAO.criarUsuario(funcionario);
    }

    public void atualizarUsuario(Funcionario funcionario) throws DatabaseException, PersistenciaException {
        if (funcionario == null || funcionario.getId() == 0) {
            throw new PersistenciaException("Funcionário inválido para atualização.");
        }
        usuarioDAO.atualizarSenhaOuCargo(funcionario);
    }

    public void deletarUsuarioPorFuncionario(int funcionarioId) throws DatabaseException, PersistenciaException {
        if (funcionarioId <= 0) {
            throw new PersistenciaException("ID do funcionário inválido.");
        }
        usuarioDAO.deletarPorFuncionario(funcionarioId);
    }

    public void sincronizarUsuarios() throws DatabaseException {
        usuarioDAO.sincronizarFuncionariosComUsuarios();
    }

    public boolean existeCPF(String cpf) throws PersistenciaException {
        if (cpf == null || cpf.isBlank()) {
            throw new PersistenciaException("CPF não informado.");
        }
        return usuarioDAO.existeCPF(cpf);
    }
}
