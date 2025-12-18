package sigena.model.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Period;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DataConverter {
    public static LocalDate toLocalDate(String data) {
        if(data == null || data.isBlank())
            return null;
        
        LocalDate dataOb = LocalDate.parse(data);
        return dataOb;
    }
    
    public static LocalDateTime toLocalDateTime(String data) {
        if(data == null || data.isBlank())
            return null;
        
        LocalDateTime dataOb = LocalDateTime.parse(data);
        return dataOb;
    }
    
    public static String toStringFormat(LocalDate data) {
        if(data == null)
            return null;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataString = data.format(formatter);
        return dataString;
    }
    
    public static String toStringFormat(LocalTime tempo) {
        if(tempo == null)
            return null;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String tempoString = tempo.format(formatter);
        return tempoString;
    }
    
    public static String toAge(LocalDate data) {
        if(data == null)
            return null;
        
        LocalDate hoje = LocalDate.now();
        Period periodo = Period.between(data, hoje);
        
        return String.format("%d ano(s) e %d mês(es)", periodo.getYears(), periodo.getMonths());
    }
}
