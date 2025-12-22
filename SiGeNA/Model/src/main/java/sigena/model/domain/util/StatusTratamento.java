package sigena.model.domain.util;

public enum StatusTratamento {
    EM_ANDAMENTO("Em_andamento"),
    CONCLUIDO("Concluido"),
    CANCELADO("Cancelado");
    
    private String status;
    
    StatusTratamento(String status){
        this.status = status;
    }
    
    public String getStatus(){
        return status;
    } 
}
