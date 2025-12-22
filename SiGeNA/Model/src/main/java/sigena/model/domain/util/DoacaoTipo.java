package sigena.model.domain.util;

public enum DoacaoTipo {
    MONETARIA,
    OUTRO;

    public static DoacaoTipo fromString(String value) {
        if (value == null) return OUTRO;
        try {
            return DoacaoTipo.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return OUTRO;
        }
    }
}
