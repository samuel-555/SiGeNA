<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="sigena.model.domain.Cargo" %>
<%@ include file="/WEB-INF/jspf/permissoes.jspf" %>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    Cargo cargo = (Cargo) sessao.getAttribute("cargoUsuario");
    boolean podeGerenciarVisitantes = temPermissaoCadastro(cargo, "visitantes");
    request.setAttribute("podeGerenciarVisitantes", podeGerenciarVisitantes);
    sigena.model.domain.Visita visitaEdicao = (sigena.model.domain.Visita) request.getAttribute("visitaEdicao");
    sigena.model.domain.Visita dadosFormulario = (sigena.model.domain.Visita) request.getAttribute("dadosFormulario");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>SiGeNA - Gestão de Visitantes</title>
    <link rel="stylesheet" href="CSS/style.css">
    <link rel="stylesheet" href="CSS/stylevisitantes.css">
</head>
<body>
<header>
    <div class="titulo">
        <a href="<%= request.getContextPath() + "/home.jsp" %>">SiGeNA</a>
    </div>
</header>

<div class="container">
    <h1>Gestão de Visitantes</h1>

    <c:if test="${not empty mensagemSucesso}">
        <div class="mensagem sucesso">${mensagemSucesso}</div>
    </c:if>
    <c:if test="${not empty mensagemErro}">
        <div class="mensagem erro">${mensagemErro}</div>
    </c:if>

    <div class="formulario" id="form-visita">
        <h2><c:choose><c:when test="${visitaEdicao != null}">Editar Registro de Visita</c:when><c:otherwise>Registrar Novo Visitante</c:otherwise></c:choose></h2>
        <c:choose>
            <c:when test="${podeGerenciarVisitantes}">
                <form action="visitantes" method="post">
                    <input type="hidden" name="acao" value="${visitaEdicao != null ? 'atualizar' : 'cadastrar'}">
                    <c:if test="${visitaEdicao != null}">
                        <input type="hidden" name="id" value="${visitaEdicao.id}">
                    </c:if>

                    <label>Nome do Visitante:</label>
                    <input type="text" name="nome" required value="<%= visitaEdicao != null ? visitaEdicao.getNomeVisitante() : (dadosFormulario != null ? dadosFormulario.getNomeVisitante() : "") %>" placeholder="Ex: Joao Silva">

                    <label>Documento (RG/CPF):</label>
                    <input type="text" name="documento" value="<%= visitaEdicao != null ? visitaEdicao.getDocumento() : (dadosFormulario != null ? dadosFormulario.getDocumento() : "") %>" placeholder="Ex: 123.456.789-00">

                    <label>Motivo da Visita:</label>
                    <input type="text" name="motivo" required value="<%= visitaEdicao != null ? visitaEdicao.getMotivo() : (dadosFormulario != null ? dadosFormulario.getMotivo() : "") %>" placeholder="Ex: Visita ao animal Rex">

                    <label>Data da Visita:</label>
                    <input type="date" name="dataVisita" required value="<%= visitaEdicao != null && visitaEdicao.getDataVisita() != null ? visitaEdicao.getDataVisita().toString() : (dadosFormulario != null && dadosFormulario.getDataVisita() != null ? dadosFormulario.getDataVisita().toString() : "") %>">

                    <label>Observações:</label>
                    <textarea name="observacoes" rows="3" placeholder="Observacoes adicionais"><%= visitaEdicao != null ? (visitaEdicao.getObservacoes() != null ? visitaEdicao.getObservacoes() : "") : (dadosFormulario != null && dadosFormulario.getObservacoes() != null ? dadosFormulario.getObservacoes() : "") %></textarea>

                    <label>Turno:</label>
                    <select name="turno" required>
                        <option value="">Selecione</option>
                        <option value="MANHA" <%= (visitaEdicao != null && sigena.model.domain.Turno.MANHA.equals(visitaEdicao.getTurno())) || (dadosFormulario != null && sigena.model.domain.Turno.MANHA.equals(dadosFormulario.getTurno())) ? "selected" : "" %>>Manhã</option>
                        <option value="TARDE" <%= (visitaEdicao != null && sigena.model.domain.Turno.TARDE.equals(visitaEdicao.getTurno())) || (dadosFormulario != null && sigena.model.domain.Turno.TARDE.equals(dadosFormulario.getTurno())) ? "selected" : "" %>>Tarde</option>
                        <option value="NOITE" <%= (visitaEdicao != null && sigena.model.domain.Turno.NOITE.equals(visitaEdicao.getTurno())) || (dadosFormulario != null && sigena.model.domain.Turno.NOITE.equals(dadosFormulario.getTurno())) ? "selected" : "" %>>Noite</option>
                    </select>

                    <div class="checks-linha">
                        <label class="checkbox-inline">
                            <input type="checkbox" name="vip" value="true" <%= (visitaEdicao != null && visitaEdicao.isVip()) || (dadosFormulario != null && dadosFormulario.isVip()) ? "checked" : "" %> />
                            Tratamento VIP
                        </label>
                        <label class="checkbox-inline">
                            <input type="checkbox" name="necessidadeEspecial" id="necessidadeEspecial" value="true" <%= (visitaEdicao != null && visitaEdicao.isNecessidadeEspecial()) || (dadosFormulario != null && dadosFormulario.isNecessidadeEspecial()) ? "checked" : "" %> onclick="toggleNecessidade()" />
                            Necessidades especiais
                        </label>
                    </div>

                    <div id="campoNecessidade" style="<%= (visitaEdicao != null && visitaEdicao.isNecessidadeEspecial()) || (dadosFormulario != null && dadosFormulario.isNecessidadeEspecial()) ? "" : "display:none;" %>">
                        <label>Descreva a necessidade especial:</label>
                        <textarea name="descricaoNecessidade" rows="2" placeholder="Ex: cadeirante, acompanhamento mï¿½dico"><%= visitaEdicao != null ? (visitaEdicao.getDescricaoNecessidade() != null ? visitaEdicao.getDescricaoNecessidade() : "") : (dadosFormulario != null && dadosFormulario.getDescricaoNecessidade() != null ? dadosFormulario.getDescricaoNecessidade() : "") %></textarea>
                    </div>

                    <div class="acoes-form">
                        <button type="submit" class="btn">Salvar Registro</button>
                        <c:if test="${visitaEdicao != null}">
                            <a href="visitantes" class="btn secundario">Cancelar edição</a>
                        </c:if>
                    </div>
                </form>
            </c:when>
            <c:otherwise>
                <p class="mensagem erro">Apenas gestores podem registrar, editar ou excluir visitas. Utilize o histórico para consulta.</p>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="resumo">
        <div class="card-resumo">
            <span class="titulo-card">Visitas hoje</span>
            <strong>${visitasHoje}</strong>
        </div>
        <div class="card-resumo">
            <span class="titulo-card">Total</span>
            <strong>${totalVisitas}</strong>
        </div>
        <div class="card-resumo">
            <span class="titulo-card">Encontradas</span>
            <strong>${totalFiltrado}</strong>
        </div>
    </div>

    <div class="filtros" id="filtros">
        <form action="visitantes" method="get" class="filtro-form">
            <div class="campo-filtro">
                <label for="ordenacao">Ordenar por:</label>
                <select name="ordenacao" id="ordenacao">
                    <option value="recentes" <%= "recentes".equals(String.valueOf(request.getAttribute("ordenacaoSelecionada"))) ? "selected" : "" %>>Mais recente</option>
                    <option value="antigas" <%= "antigas".equals(String.valueOf(request.getAttribute("ordenacaoSelecionada"))) ? "selected" : "" %>>Mais antigo</option>
                </select>
            </div>
            <div class="campo-filtro">
                <label>Periodo:</label>
                <input type="date" name="inicio" value="<%= request.getAttribute("inicioFiltro") != null ? request.getAttribute("inicioFiltro").toString() : "" %>">
                <span class="separador">ate</span>
                <input type="date" name="fim" value="<%= request.getAttribute("fimFiltro") != null ? request.getAttribute("fimFiltro").toString() : "" %>">
            </div>
            <div class="campo-filtro">
                <label for="busca">Busca:</label>
                <input type="text" name="busca" id="busca" placeholder="Nome ou documento" value="<%= request.getAttribute("buscaFiltro") != null ? request.getAttribute("buscaFiltro") : "" %>">
            </div>
            <button type="submit" class="btn">Filtrar</button>
        </form>
    </div>

    <div class="historico" id="historico">
        <h2>Historico de Visitas</h2>
        <table>
            <thead>
            <tr>
                <th>Nome</th>
                <th>Documento</th>
                <th>Motivo</th>
                <th>VIP</th>
                <th>Necessidades Especiais</th>
                <th>Data</th>
                <th>Acoes</th>
            </tr>
            </thead>
            <tbody>
            <%
                java.util.List<sigena.model.domain.Visita> visitas =
                    (java.util.List<sigena.model.domain.Visita>) request.getAttribute("visitas");
                if (visitas != null && !visitas.isEmpty()) {
                    for (sigena.model.domain.Visita v : visitas) {
            %>
            <tr>
                <td><%= v.getNomeVisitante() %></td>
                <td><%= v.getDocumento() != null ? v.getDocumento() : "-" %></td>
                <td><%= v.getMotivo() %></td>
                <td><%= v.isVip() ? "Sim" : "Não" %></td>
                <td><%= v.isNecessidadeEspecial() ? (v.getDescricaoNecessidade() != null ? v.getDescricaoNecessidade() : "Sim") : "Não" %></td>
                <td><%= v.getDataVisita() != null ? v.getDataVisita().toString() : "-" %></td>
                <td>
                    <c:choose>
                        <c:when test="${podeGerenciarVisitantes}">
                            <a class="btn-pequeno editar" href="visitantes?acao=editar&id=<%= v.getId() %>">Editar</a>
                            <form action="visitantes" method="post" style="display:inline" onsubmit="return confirm('Confirmar cancelamento?');">
                                <input type="hidden" name="acao" value="excluir">
                                <input type="hidden" name="id" value="<%= v.getId() %>">
                                <button type="submit" class="btn-pequeno excluir">Cancelar</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <span class="acao-restrita">Somente leitura</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="5">Nenhum registro encontrado.</td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>
</div>
<script>
    function toggleNecessidade() {
        const check = document.getElementById('necessidadeEspecial');
        const campo = document.getElementById('campoNecessidade');
        if (check && campo) {
            campo.style.display = check.checked ? 'block' : 'none';
        }
    }
    toggleNecessidade();
</script>
</body>
</html>
