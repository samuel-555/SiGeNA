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
    private Funcionario destinatario;
    private final LocalDateTime dataCadastro;
    private LocalDateTime dataPConclusao;
    
    public Tarefa(){
        concluida = false;
        dataCadastro = LocalDateTime.now();
    }
    
    public Tarefa(String nome, String texto, Funcionario destinatario, LocalDateTime dataPConclusao){
        this.nome = nome;
        this.texto = texto;
        this.destinatario = destinatario;
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
    public Funcionario getDestinatario(){
        return destinatario;
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
    
    
    public void setNome(String nome){
        this.nome = nome;
    }
    public void setTexto(String texto){
        this.texto = texto;
    }
    public void setDestinatario(Funcionario destinatario){
        this.destinatario = destinatario;
    }
    public void setConcluida(boolean concluida){
        this.concluida = concluida;
    }
    public void setDataPConclusao(LocalDateTime data){
        this.dataPConclusao = data;
    }
}
