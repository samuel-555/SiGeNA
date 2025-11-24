/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sigena.model.domain;
import java.time.LocalDateTime;

public class Tarefa {
    
    private long id;
    private String nome;
    private String texto;
    private boolean concluida;
    private int id_destinatario;
    private final LocalDateTime dataCadastro;
    private LocalDateTime dataPConclusao;
    
    public Tarefa(String nome, String texto, boolean concluida, int id_destinatario,LocalDateTime dataCadastro ,LocalDateTime dataPConclusao){
        this.nome = nome;
        this.texto = texto;
        this.id_destinatario = id_destinatario;
        this.concluida = concluida;
        this.dataCadastro = dataCadastro;
        this.dataPConclusao = dataPConclusao;
    }
    
    public Tarefa(String nome, String texto, int id_destinatario, LocalDateTime dataPConclusao){
        this.nome = nome;
        this.texto = texto;
        this.id_destinatario = id_destinatario;
        concluida = false;
        dataCadastro = LocalDateTime.now();
        this.dataPConclusao = dataPConclusao;
    }
    
    
    public long getId(){
        return id;
    }
    public String getNome(){
        return nome;
    }
    public String getTexto(){
        return texto;
    }
    public int getIdDestinatario(){
        return id_destinatario;
    }
    public boolean getConcluida(){
        return concluida;
    }
    public LocalDateTime getDataCadastro(){
        return dataCadastro;
    }
    public LocalDateTime getDataPConclusao(){
        return dataPConclusao;
    }
    
    
    public void setId(long id){
        this.id = id;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public void setTexto(String texto){
        this.texto = texto;
    }
    public void setIdDestinatario(int id_destinatario){
        this.id_destinatario = id_destinatario;
    }
    public void setConcluida(boolean concluida){
        this.concluida = concluida;
    }
    public void setDataPConclusao(LocalDateTime data){
        this.dataPConclusao = data;
    }
}
