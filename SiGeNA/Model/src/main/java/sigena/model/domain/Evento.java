package sigena.model.domain;
import java.time.LocalDateTime;
import sigena.model.common.util.DataConverter;

public class Evento {
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataProgramada;
    private boolean ocorrido;
    private LocalDateTime dataInsercao;
    private boolean cancelado;
    
    public Evento(String titulo, String descricao, String dataProgramada) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataProgramada = DataConverter.toLocalDateTime(dataProgramada);
        this.ocorrido = false;
        this.cancelado = false;
    }

    public Evento(Long id, String titulo, String descricao, String dataProgramada, boolean ocorrido, String dataInsercao, boolean cancelado) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataProgramada = DataConverter.toLocalDateTime(dataProgramada);
        this.ocorrido = ocorrido;
        this.dataInsercao = DataConverter.toLocalDateTime(dataInsercao);
        this.cancelado = cancelado;
    }
    
    public Evento(Long id, String titulo, String descricao, String dataProgramada) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataProgramada = DataConverter.toLocalDateTime(dataProgramada);
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getDataProgramada() {
        return dataProgramada;
    }
    
    public String getDataProgramadaFormat() {
        return DataConverter.toStringFormat(dataProgramada.toLocalDate());
    }
    
    public String getHoraProgramadaFormat() {
        return DataConverter.toStringFormat(dataProgramada.toLocalTime());
    }

    public boolean isOcorrido() {
        return ocorrido;
    }

    public LocalDateTime getDataInsercao() {
        return dataInsercao;
    }

    public boolean isCancelado() {
        return cancelado;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setDataProgramada(LocalDateTime dataProgramada) {
        this.dataProgramada = dataProgramada;
    }

    public void setOcorrido(boolean ocorrido) {
        this.ocorrido = ocorrido;
    }

    public void setDataInsercao(LocalDateTime dataInsercao) {
        this.dataInsercao = dataInsercao;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }
}
