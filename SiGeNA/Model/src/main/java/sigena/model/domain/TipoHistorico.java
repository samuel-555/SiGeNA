/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sigena.model.domain;

import java.sql.Date;
import sigena.model.domain.util.TipoTratamento;


public enum TipoHistorico {

    ESTADO {
        @Override
        public String getDescricao(EstadoFuncionario estadoAntigo, EstadoFuncionario estadoNovo){
            return "Estado alterado de " + estadoAntigo.name() + "para "+ estadoNovo.name();
        }
    },
    MANUTENCAO {
        @Override
        public String getDescricao(String nome) {
            return "Manutenção em "+ nome +" concluída";
        }
    },

    ANIMAL {
        @Override
        public String getDescricao(Animal animal, Habitat destino) {
            return "Animal " + animal.getNome() +
                   " movido para o habitat " + destino.getNome();
        }
    },
    PLANOALIMENTAR {
        @Override 
        public String getDescricao(Animal animal){
            return "Novo plano alimentar criado para "+ animal.getNome();
        }
    },
    TAREFA {
        @Override
        public String getDescricao(String tarefa) {
            return "Tarefa \"" + tarefa + "\" concluída";
        }
    },
    TRATAMENTO{
        @Override
        public String getDescricao(String diagnostico, String medicacao,int frequencia,String observacao, TipoTratamento tipo, Animal animal){
          return "Novo tratamento para "+animal
                  + "Tipo: "+tipo
                  + "Diagnostico: "+diagnostico
                  + "Medicacao: " +medicacao+" "+frequencia +"vezes"
                  + "Observação: " +observacao;
      }
    },
    RELATORIO{
        @Override
        public String getDescricao(Animal animal, double peso,String status, String observacoes){
            return "Novo relatório de saúde de "+animal.getNome()
                    +"Peso: "+peso
                    +"Status: "+status
                    +"Observações: "+observacoes;
        }
    },
    DOACAO{
        @Override
        public String getDescricao(String doador, DoacaoTipo tipo, double valor, String descricaoOutro, String observacao){
            return "Doação registrada"
                    +"Doador: "+doador
                    +"Tipo: "+tipo
                    +"Valor: "+valor
                    +"Descricao: "+descricaoOutro
                    +"Observações: "+observacao;
        }
    },
    VISITA{
        @Override
        public String getDescricao(String nome, String motivo, Date data, String observacao, Boolean vip){
            return "Visita agendadada"
                    +"Visitante: "+nome
                    +"Motivo: "+motivo
                    +"Data: "+data
                    +"Observações: "+observacao
                    +"VIP: "+vip;
        }
    },
    OCORRENCIA{
      @Override
      public String getDescricao(String descricao){
          return descricao;
      }
    };

    public String getDescricao(EstadoFuncionario estado,EstadoFuncionario estado2){
        throw new UnsupportedOperationException(
                "Este tipo não suporta descrição com Estado de funcionario"
        );
    }
    public String getDescricao(Habitat habitat) {
        throw new UnsupportedOperationException(
                "Este tipo não suporta descrição com Habitat"
        );
    }

    public String getDescricao(Animal animal, Habitat habitat) {
        throw new UnsupportedOperationException(
                "Este tipo não suporta descrição com Animal e Habitat"
        );
    }

    public String getDescricao(Animal animal){
        throw new UnsupportedOperationException(
               "Este tipo não suporta descrição com Animal"
        );
    }
    public String getDescricao(Tarefa tarefa) {
        throw new UnsupportedOperationException(
                "Este tipo não suporta descrição com Tarefa"
        );
    }
    public String getDescricao(String diagnostico, String medicacao,int frequencia,String observacao, TipoTratamento tipo, Animal animal){
        throw new UnsupportedOperationException(
                "Este tipo não suporta descrição com diagnostico, medicação, frequencia, observação, tipo e animal"
        );
    }
    public String getDescricao(Animal animal, double peso,String status, String observacoes){
        throw new UnsupportedOperationException(
                "Este tipo não suporta descrição com Animal, peso, status e observações"
        );
    }
    public String getDescricao(String doador, DoacaoTipo tipo, double valor, String descricaoOutro, String observacao){
        throw new UnsupportedOperationException(
                "Este tipo não suporta descriçao com doador, tipo, valor, descricao e observações"
        );
    }
    public String getDescricao(String nome, String motivo, Date data, String observacao, Boolean vip){
        throw new UnsupportedOperationException(
                "Este tipo não suporta descrição com nome, motivo, data, observações e VIP"
        );
    }
    public String getDescricao(String descricao){
        throw new UnsupportedOperationException(
                "Este tipo não suporta descrição com descrição da ocorrência"
        );
    }
    
    public static TipoHistorico from(String valor) {
        if (valor == null) return null;

        try {
            return TipoHistorico.valueOf(valor.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

