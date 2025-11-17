package sigena.model.domain.util;

public enum StatusTratamento {
    EM_ANDAMENTO("Em_andamento"),
    CONCLUIDO("Concluido"),
    ENCERRADO("Encerrado");
    
    private String status;
    
    StatusTratamento(String status){
        this.status = status;
    }
    
    public String getStatus(){
        return status;
    }
    
    
    
    
}
