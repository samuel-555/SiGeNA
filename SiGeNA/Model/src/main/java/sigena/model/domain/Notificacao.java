package sigena.model.domain;

import java.time.LocalDateTime;

public class Notificacao {
    private Long id;
    private String titulo;
    private boolean lida;
    private int idDestinatario;
    private LocalDateTime data;
    
    public Notificacao(){
        
    }
    
    public Notificacao(int id, String titulo){
        idDestinatario = id;
        this.titulo = titulo;
    }
    
    public Notificacao(Long id, String titulo, int idDestinatario, LocalDateTime data){
        this.id = id;
        this.titulo = titulo;
        this.idDestinatario = idDestinatario;
       this.data = data;
    }
    
    public void setId(Long id){
        this.id = id;
    }        
    
    public Long getId(){
        return id;
    }
    
    public void setTitulo(String titulo){
        this.titulo = titulo;
    }        
    
    public String getTitulo(){
        return titulo;
    }
    
    public void setLida(boolean lida){
        this.lida = lida;
    }        
    
    public boolean getLida(){
        return lida;
    }
    
    public void setIdDestinatario(int id){
        this.idDestinatario = id;
    }        
    
    public int getIdDestinatario(){
        return idDestinatario;
    }
    
    public void setData(LocalDateTime data){
        this.data = data;
    }        
    
    public LocalDateTime getData(){
        return data;
    }
    
}
