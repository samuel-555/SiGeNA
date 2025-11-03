package sigena.model.domain;

public enum Cargo {
    GERENTE("Gerente"),
    ZOOTECNISTA("Zootecnista"),
    TRATADOR("Tratador de Animais"),
    VETERINARIO("Veterinário");

    private final String descricao;

    Cargo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
