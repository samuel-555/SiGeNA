package sigena.model.service;
import sigena.model.dao.UsuarioDAO;
import sigena.model.common.exception.PersistenciaException;

public class GestaoUsuarioService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public boolean autenticar(String cpf, String senha) throws PersistenciaException {
        if (cpf == null || senha == null || cpf.isEmpty() || senha.isEmpty()) {
            throw new PersistenciaException("CPF e senha são obrigatórios!");
        }
        return usuarioDAO.autenticar(cpf, senha);
    }

    public String cadastrar(String cpf, String senha) throws PersistenciaException {
        if (cpf == null || cpf.isBlank() || senha == null || senha.isBlank()) {
            throw new PersistenciaException("CPF e senha não podem estar vazios.");
        }
        
        if (usuarioDAO.existeCPF(cpf)) {
            return "CPF já cadastrado! Não é permitido mais de um cadastro com o mesmo CPF.";
        }

        return usuarioDAO.cadastrar(cpf, senha);
    }

    public boolean existeCPF(String cpf) throws PersistenciaException {
        return usuarioDAO.existeCPF(cpf);
    }
}