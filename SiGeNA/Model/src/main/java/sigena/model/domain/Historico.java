/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sigena.model.domain;
import java.time.LocalDateTime;

public class Historico {
    
    private final long id;
    private final String descricao;
    private final int id_funcionario;
    private final LocalDateTime data;
    private final TipoHistorico tipo;
    
    public Historico(long id, String descricao, int id_funcionario, LocalDateTime data, TipoHistorico tipo){
        this.id = id;
        this.descricao = descricao;
        this.id_funcionario = id_funcionario;
        this.data = data;
        this.tipo = tipo;
    }

    public Historico(String descricao, LocalDateTime data, TipoHistorico tipo) {
        this.descricao = descricao;
        this.data = data;
        this.tipo = tipo;
    }
    
    public long getId(){
        return id;
    }
    public String getDescricao(){
        return descricao;
    }
    public int getIdFuncionario(){
        return id_funcionario;
    }
    public LocalDateTime getData(){
        return data;
    }
    public TipoHistorico getTipo(){
        return tipo;
    }
    
}
