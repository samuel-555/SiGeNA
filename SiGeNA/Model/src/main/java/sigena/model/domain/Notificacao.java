package sigena.model.domain;

import java.time.LocalDateTime;

public class Notificacao {
    private Long id;
    private String titulo;
    private String descricao;
    private boolean lida;
    private int idDestinatario;
    private LocalDateTime data;
    
    public Notificacao(){
        
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
    
    public void setDescricao(String descricao){
        this.descricao = descricao;
    }        
    
    public String getDescricao(){
        return descricao;
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
