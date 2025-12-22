package sigena.model.domain.util;

public enum Cargo {
    GERENTE("Gerente"),
    ZOOTECNISTA("Zootecnista"),
    TRATADOR("Tratador"),
    VETERINARIO("Veterinário");

    private final String descricao;

    Cargo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
