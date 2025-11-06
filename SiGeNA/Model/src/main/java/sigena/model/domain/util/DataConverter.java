package sigena.model.domain.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Period;

public class DataConverter {
    public static LocalDate toLocalDate(String data) {
        if(data == null)
            return null;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataOb = LocalDate.parse(data, formatter);
        return dataOb;
    }
    
    public static String toString(LocalDate data) {
        if(data == null)
            return null;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataString = data.format(formatter);
        return dataString;
    }
    
    public static String toAge(LocalDate data) {
        LocalDate hoje = LocalDate.now();
        Period periodo = Period.between(data, hoje);
        
        return String.format("%d ano(s) e %d mês(es)", periodo.getYears(), periodo.getMonths());
    }
}
