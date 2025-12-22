<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    boolean gerente = "GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario")));
%>
<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>SiGeNA - Gestao de Agendamentos</title>
        <link rel="stylesheet" href="CSS/styleespecies.css">
        <link rel="stylesheet" href="CSS/style.css">
        <link rel="stylesheet" href="CSS/stylehome.css">
        <link rel="stylesheet" href="CSS/stylefuncionalidades.css">
    </head>
    <body>
        <header class="topbar">
            <a href="TarefaController" class="titulo">
                <img src="IMG's/logoSiGeNA-COR2.png" alt="Logo" class="brand-logo">
                <span>SiGeNA</span>
            </a>
            <div class="user-area">
                <a href="LogoutServlet" class="btn-sair">Sair</a>
            </div>
        </header>

        <div class="container">
            <h1>Gestao de Agendamentos</h1>

            <div class="botoes-acoes">
                <% if (gerente) { %>
                    <a href="AgendamentoController?acao=cadastrar" class="btn">Cadastrar Agendamento</a>
                <% } %>
            </div>

            <div class="historico">
                <form action="AgendamentoController" method="get" class="filtro-agendamentos">
                    <input type="hidden" name="acao" value="listar">
                    <input type="text" name="busca" placeholder="Buscar por responsavel" value="${busca}">
                    <select name="tipo">
                        <option value="" ${empty tipoFiltro ? "selected" : ""}>Todos os tipos</option>
                        <option value="VISITA" ${"VISITA" == tipoFiltro ? "selected" : ""}>Visita Guiada</option>
                        <option value="TRATAMENTO" ${"TRATAMENTO" == tipoFiltro ? "selected" : ""}>Tratamento</option>
                        <option value="OUTRO" ${"OUTRO" == tipoFiltro ? "selected" : ""}>Outro</option>
                    </select>
                    <select name="ordem">
                        <option value="" ${empty ordem ? "selected" : ""}>Ordem padrao</option>
                        <option value="nome_az" ${"nome_az" == ordem ? "selected" : ""}>Responsavel A-Z</option>
                        <option value="nome_za" ${"nome_za" == ordem ? "selected" : ""}>Responsavel Z-A</option>
                        <option value="mais_recente" ${"mais_recente" == ordem ? "selected" : ""}>Mais recente</option>
                        <option value="mais_antigo" ${"mais_antigo" == ordem ? "selected" : ""}>Mais antigo</option>
                    </select>
                    <button type="submit" class="btn-pequeno">Filtrar</button>
                    <button class="btn-pequeno"><a href="AgendamentoController">Limpar</a></button>
                </form>

                <c:if test="${not empty sessionScope.acaoBemSucedida}">
                    <p class="sucesso"><c:out value="${sessionScope.acaoBemSucedida}"/></p>
                    <c:remove var="acaoBemSucedida" scope="session"/>
                </c:if>
                <c:if test="${not empty sessionScope.campoInvalidoErro}">
                    <p class="mensagem"><c:out value="${sessionScope.campoInvalidoErro}"/></p>
                    <c:remove var="campoInvalidoErro" scope="session"/>
                </c:if>

                <h2>Catalogo de Agendamentos</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Tipo</th>
                            <th>Data</th>
                            <th>Hora</th>
                            <th>Responsavel</th>
                            <th>Local</th>
                            <th>Status</th>
                            <th>Acoes</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty agendamentos}">
                                <tr>
                                    <td colspan="7">Nenhum agendamento cadastrado.</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="agendamento" items="${agendamentos}">
                                    <tr>
                                        <td><c:out value="${agendamento.tipo}"/></td>
                                        <td><c:out value="${agendamento.data}"/></td>
                                        <td><c:out value="${agendamento.hora}"/></td>
                                        <td><c:out value="${agendamento.responsavel}"/></td>
                                        <td><c:out value="${agendamento.local}"/></td>
                                        <td><c:out value="${agendamento.status}"/></td>
                                        <td>
                                            <div class="acoes-agendamento">
                                                <a href="AgendamentoController?acao=ver&id=${agendamento.id}" class="btn-pequeno">Ver</a>
                                                <% if (gerente) { %>
                                                    <c:if test="${agendamento.status == 'ATIVO'}">
                                                        <form action="AgendamentoController" method="post" class="botao-acao" onsubmit="return solicitarJustificativa(this);">
                                                            <input type="hidden" name="acao" value="cancelar">
                                                            <input type="hidden" name="id" value="${agendamento.id}">
                                                            <input type="hidden" name="justificativa" value="">
                                                            <button type="submit" class="btn-pequeno excluir">Cancelar</button>
                                                        </form>
                                                    </c:if>
                                                <% } %>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
    <script>
        function solicitarJustificativa(form) {
            var justificativa = prompt("Informe a justificativa do cancelamento:");
            if (justificativa === null) {
                return false;
            }
            justificativa = justificativa.trim();
            if (!justificativa) {
                alert("A justificativa e obrigatoria.");
                return false;
            }
            var campo = form.querySelector("input[name='justificativa']");
            if (campo) {
                campo.value = justificativa;
            }
            return true;
        }
    </script>
</html>
