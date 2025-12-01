package sigena.model.service;

import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Fornecedor;
import sigena.model.dao.FornecedorDAO;
import sigena.model.domain.util.FornecedorTipo;

public class GestaoFornecedorService {

    private final FornecedorDAO fornecedorDAO = new FornecedorDAO();

    public boolean cadastrarFornecedor(Fornecedor fornecedor) throws PersistenciaException {
        if (!conferirCampos(fornecedor))
            return false;

        fornecedorDAO.cadastrar(fornecedor);
        return true;
    }

    public List<Fornecedor> listarFornecedores() throws PersistenciaException {
        return fornecedorDAO.listar();
    }

    public Fornecedor buscarFornecedor(Long id) throws PersistenciaException {
        return fornecedorDAO.buscarPorId(id);
    }

    public void excluirFornecedor(Long id) throws PersistenciaException {
        fornecedorDAO.excluir(id);
    }

    public boolean editarFornecedor(Fornecedor fornecedor) throws PersistenciaException {
        if (!conferirCampos(fornecedor))
            return false;

        fornecedorDAO.editar(fornecedor);
        return true;
    }

    private boolean conferirCampos(Fornecedor fornecedor) {
        if (fornecedor.getNome() == null ||
            fornecedor.getNome().replaceAll("\\s", "").equals(""))
            
            return false;
        try {
            if (fornecedor.getTipo() == null ||
                fornecedor.getTipo().replaceAll("\\s", "").equals(""))
                
                return false;

            FornecedorTipo.setTipo(fornecedor.getTipo());
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}

