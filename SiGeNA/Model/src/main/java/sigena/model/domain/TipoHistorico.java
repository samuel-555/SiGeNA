/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sigena.model.domain;


public enum TipoHistorico {
    ESTADO("Mudança de estado"),
    MANUTENCAO("Manutenção concluída"),
    ANIMAL("Animal movido"),
    PLANOALIMENTAR("Plano alimentar cadastrado"),
    TAREFA("Conclusão de tarefa"),
    TRATAMENTO("Tratamento cadastrado");

    private final String descricao;

    TipoHistorico(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
