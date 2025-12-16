<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="sigena.model.domain.Enriquecimento"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Detalhes do Enriquecimento</title>
        <link rel="stylesheet" href="CSS/styleespecies.css">
        <link rel="stylesheet" href="CSS/style.css">
    </head>
    <body>
        <header>
            <div class="titulo">
                <a href="<%= request.getContextPath() + "/home.jsp"%>">
                    SiGeNA
                </a>
            </div>
        </header>

        <div class="container">
            <h1>Detalhes do Enriquecimento</h1>

            <%
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
                <a class="btn" href="${pageContext.request.contextPath}/enriquecimento?action=editar&id=<%= e.getId()%>">Editar</a>
                <a class="btn" href="${pageContext.request.contextPath}/enriquecimento">Voltar</a>
            </div>

            <% } else { %>
            <p>Enriquecimento não encontrado.</p>
            <% }%>
        </div>
    </body>
</html>
