package sigena.model.domain.util;

public enum TipoTratamento {
    
    REMEDIO("Remedio"), 
    CIRURGIA("Cirurgia"), 
    REABILITACAO("Reabilitacao"),
    PREVENTIVO("Preventivo");
    
    private String tipo;
    
    TipoTratamento(String tipo){
        this.tipo = tipo;
    }
    
    public String getTipo(){
        return tipo;
    }
}
