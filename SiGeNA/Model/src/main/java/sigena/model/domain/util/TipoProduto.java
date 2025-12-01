package sigena.model.domain.util;

public enum TipoProduto {
    PERECIVEL("Perecivel"),
    NAO_PERECIVEL("Nao_perecivel");
    
    private String tipo;
            
    TipoProduto(String tipo){
        this.tipo = tipo;
    }
    
    public String getTipo(){
        return tipo;
    }
}
