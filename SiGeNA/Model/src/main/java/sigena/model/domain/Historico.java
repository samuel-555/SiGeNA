/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sigena.model.domain;
import java.util.Date;
import java.sql.Timestamp;

public class Historico {
    
    private long id;
    private final String descricao;
    private String funcionarioCpf;
    private final Date data;
    private final TipoHistorico tipo;
    private String nomeFuncionario;
    private String cargo;
    
    public Historico(long id, String descricao, String funcionarioCpf,Date data, TipoHistorico tipo){
        this.id = id;
        this.descricao = descricao;
        this.funcionarioCpf = funcionarioCpf;
        this.data = data;
        this.tipo = tipo;
    }

    public Historico(String descricao,Date data, TipoHistorico tipo) {
        this.descricao = descricao;
        this.data = data;
        this.tipo = tipo;
    }
    
    public Historico(String funcionarioCpf, String descricao, Date data, TipoHistorico tipo) {
        this.descricao = descricao;
        this.data = data;
        this.tipo = tipo;
        this.funcionarioCpf = funcionarioCpf;
    }
    
    public long getId(){
        return id;
    }
    public String getDescricao(){
        return descricao;
    }
    public String getFuncionarioCpf(){
        return funcionarioCpf;
    }
    public Date getData(){
        return data;
    }
    public TipoHistorico getTipo(){
        return tipo;
    }
    public String getNomeFuncionario(){
        return nomeFuncionario;
    }
    public String getCargoFuncionario(){
        return cargo;
    }
    
    public void setNomeFuncionario(String nome){
        this.nomeFuncionario = nome;
    }
    public void setCargoFuncionario(String cargo){
        this.cargo = cargo;
    }
}
