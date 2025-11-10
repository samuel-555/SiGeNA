package sigena.model.domain.util;

public enum TipoTratamento {
    
    REMEDIO("Remedio"), 
    CIRURGIA("Cirugia"), 
    REABILITACAO("Reabilitação"),
    PREVENTIVO("Preventivo");
    
    private String tipo;
    
    TipoTratamento(String tipo){
        this.tipo = tipo;
    }
    
    public String getTipo(){
        return tipo;
    }
}
