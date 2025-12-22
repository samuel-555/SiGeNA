package sigena.model.domain.util;

public enum Turno {
    MANHA("Manhã"),
    TARDE("Tarde"),
    NOITE("Noite");

    private final String descricao;

    Turno(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
