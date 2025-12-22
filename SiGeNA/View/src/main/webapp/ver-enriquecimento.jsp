<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="sigena.model.domain.Enriquecimento"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ include file="/WEB-INF/jspf/permissoes.jspf" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Detalhes do Enriquecimento</title>
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
            <h1>Detalhes do Enriquecimento</h1>

            <%
                HttpSession sessao = request.getSession(false);
                Cargo cargo = (sessao != null) ? (Cargo) sessao.getAttribute("cargoUsuario") : null;
                boolean podeGerenciar = temPermissaoGerenciamento(cargo, "enriquecimento");
                Enriquecimento e = (Enriquecimento) request.getAttribute("enriquecimento");
                if (e != null) {
            %>

            <div class="historico">
                <p><strong>Nome:</strong> <%= e.getNome()%></p>
                <p><strong>Tipo:</strong> <%= e.getTipo()%></p>
                <p><strong>Espécie Destinada:</strong> <%= e.getEspecieDestinada()%></p>
                <p><strong>Frequência:</strong> <%= e.getFrequencia()%></p>
                <p><strong>Habitats:</strong> <%= String.join(", ", e.getHabitats())%></p>
                <p><strong>Observações:</strong></p>
                <div class="observacoes">
                    <%= e.getObservacoes() != null ? e.getObservacoes() : "Sem observações."%>
                </div>
            </div>

            <div class="botoes-acoes">
                <% if (podeGerenciar) { %>
                <a class="btn" href="${pageContext.request.contextPath}/enriquecimento?action=editar&id=<%= e.getId()%>">Editar</a>
                <% } %>
                <a class="btn" href="${pageContext.request.contextPath}/enriquecimento">Voltar</a>
            </div>

            <% } else { %>
            <p>Enriquecimento não encontrado.</p>
            <% }%>
        </div>
    </body>
</html>
