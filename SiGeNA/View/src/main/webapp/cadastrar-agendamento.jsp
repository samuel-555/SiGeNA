<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    if (!"GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario")))) {
        response.sendRedirect("home.jsp");
        return;
    }
    boolean edicao = request.getAttribute("agendamento") != null;
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
        <a href="home.jsp" class="titulo">
            <img src="IMG's/logoSiGeNA-COR2.png" alt="Logo" class="brand-logo">
            <span>SiGeNA</span>
        </a>
        <div class="user-area">
            <a href="LogoutServlet" class="btn-sair">Sair</a>
        </div>
    </header>

        <div class="container">
            <h1>Gestao de Agendamentos</h1>
             <a href="AgendamentoController?acao=listar" class="btn-sair" style="background: var(--zoo-mint); color: var(--zoo-dark-green); margin-right: 10px;">Voltar</a>
            <div class="formulario">
                <h2><%= edicao ? "Editar Agendamento" : "Marcar Novo Agendamento" %></h2>
                <c:if test="${not empty sessionScope.campoInvalidoErro}">
                    <p class="mensagem"><c:out value="${sessionScope.campoInvalidoErro}"/></p>
                    <c:remove var="campoInvalidoErro" scope="session"/>
                </c:if>
                <form action="AgendamentoController" method="post">
                    <c:if test="${not empty agendamento.id}">
                        <input type="hidden" name="id" value="${agendamento.id}">
                    </c:if>
                    <input type="hidden" name="acao" value="<%= edicao ? "atualizar" : "salvar" %>">

                    <label for="tipo">Tipo de Agendamento:</label>
                    <select id="tipo" name="tipo" required>
                        <option value="">Selecione</option>
                        <option value="VISITA" <c:if test="${agendamento.tipo == 'VISITA'}">selected</c:if>>Visita Guiada</option>
                        <option value="TRATAMENTO" <c:if test="${agendamento.tipo == 'TRATAMENTO'}">selected</c:if>>Tratamento</option>
                        <option value="OUTRO" <c:if test="${agendamento.tipo == 'OUTRO'}">selected</c:if>>Outro</option>
                    </select>

                    <label for="data">Data:</label>
                    <input type="date" id="data" name="data" value="<c:out value='${agendamento.data}'/>" required>

                    <label for="hora">Hora:</label>
                    <input type="time" id="hora" name="hora" value="<c:out value='${agendamento.hora}'/>" required>

                    <label for="responsavel">Responsavel:</label>
                    <input type="text" id="responsavel" name="responsavel" placeholder="Ex: Joao Silva" value="<c:out value='${agendamento.responsavel}'/>" required>

                    <label for="local">Local:</label>
                    <input type="text" id="local" name="local" placeholder="Ex: Sala de atendimento" value="<c:out value='${agendamento.local}'/>" required>

                    <label for="observacoes">Observacoes:</label>
                    <textarea id="observacoes" name="observacoes" rows="3" placeholder="Informacoes adicionais"><c:out value='${agendamento.observacoes}'/></textarea>

                    <button type="submit" class="btn-enviar"><%= edicao ? "Atualizar Agendamento" : "Salvar Agendamento" %></button>
                </form>
            </div>        
        </div>
    </body>
</html>
