package sigena.model.domain.util;

public enum StatusDoacao {
    ATIVA,
    CANCELADA,
    FINALIZADA;

    public static StatusDoacao fromString(String value) {
        if (value == null) return ATIVA;
        try {
            return StatusDoacao.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ATIVA; 
        }
    }
}
