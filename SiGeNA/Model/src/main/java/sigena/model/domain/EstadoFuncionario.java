package sigena.model.domain;

public enum EstadoFuncionario {

    ATIVO("Ativo"),
    FERIAS("Férias"),
    LICENCA_MATERNIDADE("Licença Maternidade"),
    LICENCA_PATERNIDADE("Licença Paternidade"),
    AFASTADO("Afastado");

    private final String descricao;

    EstadoFuncionario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EstadoFuncionario getPadrao() {
        return ATIVO;
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}
