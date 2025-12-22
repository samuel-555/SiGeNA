<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="sigena.model.domain.Agendamento" %>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    boolean gerente = "GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario")));
    Agendamento agendamento = (Agendamento) request.getAttribute("agendamento");
%>
<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="UTF-8">
        <title>Detalhes do Agendamento</title>
        <link rel="stylesheet" href="CSS/styleespecies.css">
        <link rel="stylesheet" href="CSS/style.css">
        <link rel="stylesheet" href="CSS/stylehome.css">
    <link rel="stylesheet" href="CSS/stylefuncionalidades.css">
    </head>
    <body>
        <header class="topbar">
            <a href="HomeController" class="titulo">
                <img src="IMG's/logoSiGeNA-COR2.png" alt="Logo" class="brand-logo">
                <span>SiGeNA</span>
            </a>
            <div class="user-area">
                <a href="LogoutServlet" class="btn-sair">Sair</a>
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
                <% if (agendamento.getStatus() != null && "CANCELADO".equals(agendamento.getStatus().name())) { %>
                    <p><strong>Justificativa do cancelamento:</strong> <%= agendamento.getJustificativaCancelamento() != null && !agendamento.getJustificativaCancelamento().isBlank() ? agendamento.getJustificativaCancelamento() : "Sem justificativa." %></p>
                <% } %>
                <p><strong>Observacoes:</strong></p>
                <div class="observacoes">
                    <%= agendamento.getObservacoes() != null && !agendamento.getObservacoes().isBlank() ? agendamento.getObservacoes() : "Sem observacoes." %>
                </div>
            </div>

            <div class="botoes-acoes">
                <% if (gerente && (agendamento.getStatus() == null || !"CANCELADO".equals(agendamento.getStatus().name()))) { %>
                    <a class="btn" href="AgendamentoController?acao=editar&id=<%= agendamento.getId() %>">Editar</a>
                <% } %>
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
