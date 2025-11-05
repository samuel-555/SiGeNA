package sigena.model.dao;

import sigena.model.domain.Funcionario;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {
    private static final List<Funcionario> funcionarios = new ArrayList<>();

    public void salvar(Funcionario funcionario) {
        funcionarios.add(funcionario);
    }

    public List<Funcionario> listar() {
        return funcionarios;
    }

    public void remover(int id) {
        funcionarios.removeIf(f -> f.getId() == id);
    }
}
