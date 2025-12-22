package sigena.model.domain;

import java.time.LocalDateTime;

public class Tarefa {
    
    private long id;
    private String nome;
    private String texto;
    private boolean concluida;
    private int id_destinatario;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataPConclusao;
    private String cpfAutor;
    
    public Tarefa(String nome, String texto, boolean concluida, int id_destinatario, LocalDateTime dataCadastro, LocalDateTime dataPConclusao) {
        this.nome = nome;
        this.texto = texto;
        this.id_destinatario = id_destinatario;
        this.concluida = concluida;
        this.dataCadastro = dataCadastro;
        this.dataPConclusao = dataPConclusao;
    }
    
    public Tarefa(String nome, String texto, int id_destinatario, LocalDateTime dataPConclusao, String cpfAutor) {
        this.nome = nome;
        this.texto = texto;
        this.id_destinatario = id_destinatario;
        this.concluida = false;
        this.dataCadastro = LocalDateTime.now();
        this.dataPConclusao = dataPConclusao;
        this.cpfAutor = cpfAutor;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTexto() {
        return texto;
    }

    public int getIdDestinatario() {
        return id_destinatario;
    }

    public boolean getConcluida() {
        return concluida;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public LocalDateTime getDataPConclusao() {
        return dataPConclusao;
    }

    public String getCpfAutor() {
        return cpfAutor;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setIdDestinatario(int id_destinatario) {
        this.id_destinatario = id_destinatario;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public void setDataPConclusao(LocalDateTime data) {
        this.dataPConclusao = data;
    }

    public void setDataCadastro(LocalDateTime data) {
        this.dataCadastro = data;
    }

    public void setCpfAutor(String cpf) {
        this.cpfAutor = cpf;
    }

}
