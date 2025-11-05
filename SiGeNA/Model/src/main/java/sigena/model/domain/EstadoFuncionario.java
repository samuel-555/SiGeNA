package sigena.model.domain;

public enum EstadoFuncionario {
    ATIVO("Ativo", 0),
    FERIAS("Férias", 30),
    LICENCA_MATERNIDADE("Licença Maternidade", 120),
    LICENCA_PATERNIDADE("Licença Paternidade", 5),
    AFASTADO("Afastado", 0);

    private final String descricao;
    private final int diasPadrao;

    EstadoFuncionario(String descricao, int diasPadrao) {
        this.descricao = descricao;
        this.diasPadrao = diasPadrao;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getDiasPadrao() {
        return diasPadrao;
    }
}
