package sigena.model.domain.util;
import java.text.Normalizer;

public enum AnimalSexo {
    MACHO("Macho"),
    FEMEA("Fêmea"),
    INDEFINIDO("Indefinido");
    
    private String sexo;
    
    AnimalSexo(String sexo) {
        this.sexo = sexo;
    }
    
    public static AnimalSexo setAnimalSexo(String sexo) {
         switch(Normalizer.normalize(sexo.toUpperCase(), Normalizer.Form.NFD).
                 replaceAll("\\p{InCombiningDiacriticalMarks}+", "")) {
             case "MACHO":
                 return MACHO;
             case "FEMEA":
                 return FEMEA;
             case "INDEFINIDO":
                 return INDEFINIDO;
         }
         
         throw new IllegalArgumentException("Sexo não reconhecido");
    }
    
    public String getSexo() {
        return sexo;
    }
}
