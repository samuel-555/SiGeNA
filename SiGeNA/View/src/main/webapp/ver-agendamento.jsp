<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="sigena.model.domain.Agendamento" %>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    Agendamento agendamento = (Agendamento) request.getAttribute("agendamento");
%>
<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="UTF-8">
        <title>Detalhes do Agendamento</title>
        <link rel="stylesheet" href="CSS/styleespecies.css">
        <link rel="stylesheet" href="CSS/style.css">
    </head>
    <body>
        <header>
            <div class="titulo">
                <a href="<%= request.getContextPath() + ("GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "/home-gerente.jsp" : "/home.jsp")%>">
                    SiGeNA
                </a>
            </div>
        </header>

        <div class="container">
            <h1>Detalhes do Agendamento</h1>

            <% if (agendamento != null) { %>
            <div class="historico">
                <p><strong>Tipo:</strong> <%= agendamento.getTipo() %></p>
                <p><strong>Data:</strong> <%= agendamento.getData() %></p>
                <p><strong>Hora:</strong> <%= agendamento.getHora() %></p>
                <p><strong>Responsavel:</strong> <%= agendamento.getResponsavel() %></p>
                <p><strong>Local:</strong> <%= agendamento.getLocal() %></p>
                <p><strong>Status:</strong> <%= agendamento.getStatus() %></p>
                <p><strong>Observacoes:</strong></p>
                <div class="observacoes">
                    <%= agendamento.getObservacoes() != null && !agendamento.getObservacoes().isBlank() ? agendamento.getObservacoes() : "Sem observacoes." %>
                </div>
            </div>

            <div class="botoes-acoes">
                <a class="btn" href="AgendamentoController?acao=editar&id=<%= agendamento.getId() %>">Editar</a>
                <a class="btn" href="AgendamentoController?acao=listar">Voltar</a>
            </div>
            <% } else { %>
                <p>Agendamento nao encontrado.</p>
                <div class="botoes-acoes">
                    <a class="btn" href="AgendamentoController?acao=listar">Voltar</a>
                </div>
            <% } %>
        </div>
    </body>
</html>
