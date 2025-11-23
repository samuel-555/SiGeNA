package sigena.model.service;

import sigena.model.dao.TratamentoDAO;
import sigena.model.domain.Animal;
import sigena.model.domain.Tratamento;
import sigena.model.domain.Usuario;

public class GestaoTratamentosService {
    TratamentoDAO dao = new TratamentoDAO();
    
    public void cadastrar(Animal animal, Usuario usuario, Tratamento tratamento){
        dao.cadastrar(animal, usuario, tratamento);
    }
}
