package sigena.model.domain;

import sigena.model.domain.util.TipoTratamento;
import sigena.model.domain.util.StatusTratamento;
import java.time.LocalDateTime;

public class Tratamento {
    
    private int id;
    private Animal animal;
    private Usuario veterinario;
    private String diagnostico;
    private String medicacao;
    private int frequencia;
    private String observacao;
    private TipoTratamento tipoTratamento;
    private StatusTratamento statusTratamento;
    private LocalDateTime dataFinal;
    
    
    public Tratamento(Animal animal, Usuario veterinario, String diagnostico, String medicacao, int frequencia, 
            String observacao, TipoTratamento tipoTratamento, StatusTratamento statusTratamento, LocalDateTime dataFinal){
        this.animal = animal;
        this.veterinario = veterinario;
        this.diagnostico = diagnostico;
        this.medicacao = medicacao;
        this.frequencia = frequencia;
        this.observacao = observacao;      
        this.tipoTratamento = tipoTratamento;
        this.statusTratamento = statusTratamento;
        this.dataFinal = dataFinal;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public void setAnimal(Animal animal){
        this.animal = animal;
    }
    
    public Animal getAnimal(){
        return animal;
    }
    
    public void setMedico(Usuario veterinario){
        this.veterinario = veterinario;
    }
    
    public Usuario getMedico(){
        return veterinario;
    }
    
    public void setDiagnostico(String diagnostico){
        this.diagnostico = diagnostico;
    }
    
    public String getDiagnostico(){
        return diagnostico;
    }
    
    public void setMedicacao(String medicacao){
        this.medicacao = medicacao;
    }
    
    public String getMedicacao(){
        return medicacao;
    }
    
    public void setFrequencia(int frequencia){
        this.frequencia = frequencia;
    }
    
    public int getFrequencia(){
        return frequencia;
    }
    
    public void setObservacao(String observacao){
        this.observacao = observacao;
    }
    
    public String getObservacao(){
        return observacao;
    }
    
    public String getTipoTratamento() {
        return tipoTratamento.getTipo();
    }
    
    public void setTipoTratamento(TipoTratamento tipoTratamento) {
        this.tipoTratamento = tipoTratamento;
    }
    
    public String getStatusTratamento() {
        return statusTratamento.getStatus();
    }
    
    public void setStatusTratamento(StatusTratamento statusTratamento) {
        this.statusTratamento = statusTratamento;
    }
    
    public LocalDateTime getDataFinal() {
        return dataFinal;
    }
    
    public void setDataFinal(LocalDateTime dataFinal) {
        this.dataFinal = dataFinal;
    }
}