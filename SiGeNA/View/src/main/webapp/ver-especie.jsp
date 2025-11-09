<%@page import="sigena.model.domain.Especie"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Detalhes da Espécie</title>
        <link rel="stylesheet" href="CSS/styleespecies.css">
    </head>
    <body>
        <header><div class="titulo">SiGeNA</div></header>

        <div class="container">
            <h1>Detalhes da Espécie</h1>
            <%
                Especie e = (Especie) request.getAttribute("especie");
                if (e != null) {
            %>
            <div class="historico">
                <p><strong>Nome:</strong> <%= e.getNome() %></p>
                <p><strong>Classe:</strong> <%= e.getClasse() %></p>
                <p><strong>Habitat:</strong> <%= e.getHabitat() %></p>
                <p><strong>Alimentação:</strong> <%= e.getAlimentacao() %></p>
                <p><strong>Predador:</strong> <%= e.isPredador() ? "Sim" : "Não" %></p>
                <p><strong>Observações:</strong></p>
                <div class="observacoes"><%= e.getObservacoes() != null && !e.getObservacoes().isEmpty() ? e.getObservacoes() : "Sem observações." %></div>
            </div>
            <%
                } else {
            %>
            <p>Espécie não encontrada.</p>
            <%
                }
            %>

            <div class="botoes-acoes">
                <a href="EspeciesController?acao=editar&id=<%= e.getId() %>" class="btn">Editar</a>
                <a href="EspeciesController" class="btn">Voltar</a>
            </div>
        </div>
    </body>
</html>
