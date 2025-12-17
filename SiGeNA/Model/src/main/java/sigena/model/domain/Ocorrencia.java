package sigena.model.domain;

import sigena.model.domain.util.OcorrenciaTipo;
import sigena.model.domain.util.StatusOcorrencia;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ocorrencia {

    private Long id;
    private String titulo;
    private String descricao;
    private String responsavel;
    private OcorrenciaTipo tipo;
    private StatusOcorrencia status;
    private LocalDateTime data;

    public Ocorrencia() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public OcorrenciaTipo getTipo() {
        return tipo;
    }

    public void setTipo(OcorrenciaTipo tipo) {
        this.tipo = tipo;
    }

    public StatusOcorrencia getStatus() {
        return status;
    }

    public void setStatus(StatusOcorrencia status) {
        this.status = status;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }


    public String getHora() {
        if (data != null) {
            return data.toLocalTime()
                       .format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return null;
    }

    public String getDataFormatada() {
        if (data != null) {
            return data.toLocalDate()
                       .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return null;
    }
}
