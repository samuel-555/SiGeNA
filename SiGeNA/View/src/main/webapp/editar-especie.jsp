<%@page import="sigena.model.domain.Especie"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Editar Espécie</title>
        <link rel="stylesheet" href="CSS/styleespecies.css">
        <link rel="stylesheet" href="CSS\style.css">
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
            <h1>Editar Espécie</h1>

            <%
                Especie e = (Especie) request.getAttribute("especie");
                if (e != null) {
            %>
            <form action="EspeciesController" method="post">
                <input type="hidden" name="acao" value="atualizar">
                <input type="hidden" name="id" value="<%= e.getId() %>">

                <label>Nome:</label>
                <input type="text" name="nome" value="<%= e.getNome() %>" required>

                <label>Classe:</label>
                <input type="text" name="classe" value="<%= e.getClasse() %>">

                <label>Habitat:</label>
                <input type="text" name="habitat" value="<%= e.getHabitat() %>" required>

                <label>Alimentação:</label>
                <input type="text" name="alimentacao" value="<%= e.getAlimentacao() %>" required>
                
                <div class="checkbox-group">
                    <label>
                        <input type="checkbox" name="predador" value="true" <%= e.isPredador() ? "checked" : "" %>> Espécie Predadora
                    </label>
                </div>
                    
                <label>Observações:</label>
                <textarea name="observacoes" rows="4"><%= e.getObservacoes() %></textarea>

                <button type="submit" class="btn-enviar">Salvar Alterações</button>
                <a href="EspeciesController?acao=ver&id=<%= e.getId() %>" class="btn">Voltar</a>
            </form>
            <%
                } else {
            %>
            <p>Espécie não encontrada.</p>
            <a href="EspeciesController" class="btn">Voltar</a>
            <%
                }
            %>
        </div>
    </body>
</html>
