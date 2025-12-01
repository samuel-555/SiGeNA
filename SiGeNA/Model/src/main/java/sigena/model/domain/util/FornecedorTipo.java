package sigena.model.domain.util;
import java.text.Normalizer;

public enum FornecedorTipo {
    ALIMENTO("ALIMENTO"),
    MEDICAMENTO("MEDICAMENTO"),
    EQUIPAMENTO("EQUIPAMENTO"),
    HIGIENE_LIMPEZA("HIGIENE E LIMPEZA"),
    ACESSORIOS("ACESSÓRIOS"),
    SERVICOS("SERVIÇOS"),
    OUTROS("OUTROS"),
    VARIADOS("VARIADOS");
    
    private String tipo;
    
    FornecedorTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public static FornecedorTipo setTipo(String tipo) {
         switch(Normalizer.normalize(tipo.toUpperCase(), Normalizer.Form.NFD).
                 replaceAll("\\p{InCombiningDiacriticalMarks}+", "")) {
             case "ALIMENTO":
                 return ALIMENTO;
             case "MEDICAMENTO":
                 return MEDICAMENTO;
             case "EQUIPAMENTO":
                 return EQUIPAMENTO;
             case "HIGIENE E LIMPEZA":
                 return HIGIENE_LIMPEZA;
             case "ACESSORIOS":
                 return ACESSORIOS;
             case "SERVICOS":
                 return SERVICOS;
             case "OUTROS":
                 return OUTROS;
             case "VARIADOS":
                 return VARIADOS;
         }
         
         throw new IllegalArgumentException("Tipo não reconhecido");
    }
    
    public String getTipo() {
        return tipo;
    }
}
