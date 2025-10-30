# SiGeNA-Sistema de Gerenciamento de Necessidades Animais

## Desenvolvedores
| Ordem | Nome          |
|:------|:--------------|
| 1     | Alice         |
| 2     | Raphael       |
| 3     | Samuel M      |
| 4     | Sara          |
| 5     | Thauan        |
| 6     | Tiago         |

## Atores
| Ator      | Definição     |
|:----------|:--------------|
| Gerente   | Usuário com cargo de Gerente, utilizará o sistema para cadastrar e gerenciar funcionários, cadastrar e gerenciar animais, mandar notificações, agendamento de visitas guiadas e eventos.|
|Zootecnista| Usuário com cargo de Zootecnista, utilizará o sistema para monitorar os exames, dieta, genética e reprodução dos animais.|
| Tratadores de Animais | Usuário com cargo de Tratador de animais, utilizará o sistema para receber comandos como o de limpeza de recintos, horários de alimentação dos animais e etc. |
|Veterinários| Usuário com cargo de Veterinário, responsável por monitorar e garantir a saúde e bem-estar do animal, análise de espécies e planejamento de habitats.|
|Sistema|Sistema, responsável por ações automatizadas do software.|

## Requisitos Funcionais
| Id     | Ator       | Descrição   |
|:-------|:-----------|:------------|
|REQ001|Gerente|Gestão de Funcionários, adicionar funcionário (cargo, área de atuação); ver lista de funcionários; atualizar dados ou cargo; remover ex-funcionário|
|REQ002|Zootecnista|Gestão de animais, cadastrar novo animal (nome, espécie, idade, habitat, dieta, etc.); listar todos os animais ou buscar por ID/nome/espécie; alterar dados do animal (peso, saúde, jaula, etc.); remover animal do sistema.|
|REQ003|Zootecnista|Gestão de Espécies, adicionar nova espécie ao catálogo; visualizar todas as espécies; atualizar características de uma espécie; excluir uma espécie.|
|REQ004|Zootecnista, Tratadores de animais|Gestão de habitat, cadastrar novo habitat; ver lista de habitats e animais contidos nelas; modificar dados do habitat; excluir habitat vazio.|
|REQ005|Sistema, Zootecnista, Tratadores de animais, Veterinários e Gerente|Gestão de histórico, registrar tarefa cumprida; listar atendimentos e animais atendidos; listar tratamento e comportamento de animais; Consultar ou editar histórico;|
|REQ006|Veterinários|Gestão de tratamentos médicos dos animais, registrar novo tratamento para animal; ver histórico de tratamentos; alterar detalhes (medicação, frequência); remover tratamento (exemplo: cadastrado por engano)|
|REQ007|Gerente|Gestão de visitantes, registrar novo visitante; ver histórico de visitas; editar dados da visita; remover registro|
|REQ008|Zootecnista, Tratadores de animais e Veterinários|Gestão de planos alimentares, registrar nova dieta ou refeição; ver plano alimentar por espécie; ajustar cardápio; excluir plano desatualizado|
|REQ009|Gerente|Gestão de agendamentos, marcar visitas guiadas ou tratamentos; consultar agenda por data ou funcionário; alterar horário ou responsável; cancelar agendamento|
|REQ010|Gerente|Gestão de fornecedores, cadastrar fornecedor (ração, medicamentos); listar fornecedores; atualizar contatos/produtos; remover fornecedor inativo|
|REQ011|Tratadores de animais|Gestão de produtos/estoque, adicionar item ao estoque (ração, medicamentos, utensílios); verificar disponibilidade; ajustar quantidade ou validade; excluir item vencido/descontinuado|
|REQ012|Veterinários|Gestão de relatórios de saúde dos animais, criar relatório de check-up; consultar histórico de saúde; acrescentar observações; excluir relatório antigo/duplicado|
|REQ013|Gerente|Gestão de ocorrências (fuga, acidente e etc), criar ocorrências; consultar histórico de ocorrências; atualizar informações de uma ocorrência; excluir ocorrência|
|REQ014|Gerente|Gestão de doações, registrar nova doação; ver histórico de doações e doadores; atualizar valor ou finalidade; cancelar doação|
|REQ015|Gerente|Gestão de eventos, agendar evento; ver calendário de eventos; mudar data/hora; cancelar evento|
|REQ016|Zootecnistas|Gestão de enriquecimento ambiental(maneiras ou objetos que melhoram a qualidade de vida do animal), adicionar enriquecimento ambiental, consultar enriquecimento ambiental, alterar enriquecimento ambiental, desalocar enriquecimento ambiental|
|REQ017|Sistema|Gestão de notificação, adicionar notificações, consultar notificações, atualizar notificações e deletar notificações|
|REQ018|Sistema|Gestão de login, adicionar login, consultar logins, atualizar login (ex:mudar senha) e deletar login.|
|REQ019|Gerente|Cadastrar, consultar, atualizar e excluir tarefas para os funcionários|



## Regras de Negócio
| Id     | Nome       | Descrição   |
|:-------|:-----------|:------------|
|RN001|Número mínimo de funcionários|Deve haver pelo menos um funcionário por setor em cada turno.|
|RN002|Autorização especial|Cargos com acesso a áreas restritas exigem autorização especial.|
|RN003|Animal sem espécie associada|Não é permitido cadastrar um animal sem associá-lo a uma espécie e a uma habitat.|
|RN004|Animal em tratamentos|Não se pode deletar um animal enquanto ele estiver com tratamentos médicos ativos.|
|RN005|Deletar espécie sem animal vinculado|Só é possível deletar uma espécie se nenhum animal estiver vinculado a ela.|
|RN006|Habitat e dieta no registro|Deve-se cadastrar o habitat natural e dieta típica da espécie no momento do registro.|
|RN007|Habitat de animais hostis|Habitat de animais hostis não podem conter mais de uma espécie.|
|RN008|Deletar habitat com animais associados|Não é permitido deletar uma habitat com animais alocados.|
|RN009|Veterinário com agendamento|Não é permitido excluir um veterinário com agendamentos pendentes.|
|RN010|Registro profissional válido|Veterinários devem possuir registro profissional válido (exemplo: CRMV).|
|RN011|Deletar tratamento já iniciado|Não é possível deletar um tratamento já iniciado, apenas arquivá-lo como encerrado.|
|RN012|Tratamentos conflitantes|Um animal não pode ter tratamentos simultâneos conflitantes (exemplo: medicamentos incompatíveis).|
|RN013|Sinalização VIP|Visitantes VIP ou com necessidades especiais devem ter sinalização específica.|
|RN014|Acompanhamento|Grupos escolares devem ser acompanhados por um guia.|
|RN015|Exclusão de dieta|A exclusão de uma dieta só pode ocorrer se não estiver em uso.|
|RN016|Dieta apropriada|Cada animal deve seguir a dieta apropriada da espécie.|
|RN017|24h de antecedência|Cancelamentos devem ser feitos com no mínimo 24h de antecedência.|
|RN018|Horários sobrepondo|Horários de agendamento não podem se sobrepor para o mesmo profissional ou local.|
|RN019|Data de validade|Produtos com datas de validade próximas não podem ser aceitos.|
|RN020|Deletar fornecedor|Não é possível deletar fornecedor com pedidos pendentes.|
|RN021|Indisponibilidade de produtos|Produtos vencidos são automaticamente marcados como indisponíveis.|
|RN022|Lote, validade...|Cada produto deve ter lote, validade e fornecedor registrados.|
|RN023|Informação de relatórios|Relatórios devem conter data, observações e status do animal (apto/inapto).|
|RN024|Relatórios fiscalizados|Não é permitido deletar relatórios vinculados a fiscalizações.|
|RN025|Registrando ocorrência|Obrigatório registrar: tipo da ocorrência (fuga, acidente, incidente, outro), data e hora do ocorrido (sendo válidos).|
|RN026|Vínculos de doações|Todas as doações devem estar vinculadas a uma finalidade (exemplo: alimentação, infraestrutura).|
|RN027|Recibo de doações|Doações acima de certo valor devem ter recibo emitido.|
|RN028|Horários de eventos|Eventos não podem ser marcados fora do horário de visitação.|
|RN029|Notificação de exclusão|A exclusão de um evento ou tarefa deve notificar automaticamente os participantes registrados.|
|RN030|Vínculo de enriquecimento ambiental|Todo enriquecimento deve estar vinculado a um ou mais habitats.|
|RN031|Cadastro duplicado|Não é permitido mais de um cadastro com os mesmos dados|
|RN032|Datas inválidas|Para certos tipos de cadastro, deve ser informada uma data válida|
|RN033|Horário de funcionamento|O sistema só executa certas funções dentro do horário permitido|
|RN034|Editar histórico|O histórico não pode ser editado por outro usuário que não seja o autor do registro|


## Casos de Uso
| Id     | Nome       | Requisitos     | Regras de Negócio  |
|:-------|:-----------|:---------------|:-------------------|
|CSU01|Gerenciamento de funcionários|REQ001|RN001, RN002|
|CSU02|Gerenciamento de animais|REQ002|RN003, RN004|
|CSU03|Gerenciamento de espécies|REQ003|RN005, RN006|
|CSU04|Gerenciamento de habitat|REQ004|RN007, RN008|
|CSU05|Gerenciamento de histórico|REQ005|RN032, RN034|
|CSU06|Gerenciamento de tratamentos médicos dos animais|REQ006|RN011, RN012|
|CSU07|Gerenciamento de visitantes|REQ007|RN013, RN014|
|CSU08|Gerenciamento de planos alimentares|REQ008|RN015, RN016|
|CSU09|Gerenciamento de agendamentos|REQ009|RN017, RN018|
|CSU10|Gerenciamento de fornecedores|REQ010|RN019, RN020|
|CSU11|Gerenciamento de produtos/estoque|REQ011|RN021, RN022|
|CSU12|Gerenciamento de relatórios de saúde dos animais|REQ012|RN023, RN024|
|CSU13|Gerenciamento de ocorrências|REQ013|RN025|
|CSU14|Gerenciamento de doações|REQ014|RN026, RN027|
|CSU15|Gerenciamento de eventos|REQ015|RN028, RN029|
|CSU16|Gerenciamento de enriquecimento|REQ016|RN030|
|CSU17|Gerenciamento de notificação|REQ017|RN033|
|CSU18|Login|REQ018|RN031|
|CSU19|Tarefas|REQ019|RN029, RN032|


## Planejamento
| Sprint | Caso de Uso | Desenvolvedor  |
|:-------|:------------|:---------------|
| 1      | CSU04       | 1              |
| 1      | CSU08       | 2              |
| 1      | CSU01       | 3              |
| 1      | CSU06       | 4              |
| 1      | CSU02       | 5              |
| 1      | CSU03, CSU18| 6              |
|                                       |
| 2      | CSU19       | 1              |
| 2      | CSU12       | 2              |
| 2      | CSU14       | 3              |
| 2      | CSU11       | 4              |
| 2      | CSU10       | 5              |
| 2      | CSU16       | 6              |
|                                       |
| 3      | CSU05       | 1              |
| 3      | CSU07       | 2              |
| 3      | CSU13       | 3              |
| 3      | CSU17       | 4              |
| 3      | CSU15       | 5              |
| 3      | CSU09       | 6              |
