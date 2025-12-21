<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="sigena.model.domain.Enriquecimento"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Editar Enriquecimento</title>
        <link rel="stylesheet" href="CSS/styleespecies.css">
        <link rel="stylesheet" href="CSS/style.css">
    </head>
    <body>

        <header>
            <div class="titulo">
                <a href="<%= request.getContextPath() + "/home.jsp"%>">SiGeNA</a>
            </div>
        </header>

        <%
            Enriquecimento e = (Enriquecimento) request.getAttribute("enriquecimento");
            List<String> habitats = (List<String>) request.getAttribute("habitats");
        %>

        <div class="container">
            <h1>Editar Enriquecimento</h1>

            <form action="${pageContext.request.contextPath}/enriquecimento" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="<%= e.getId()%>">

                <label>Nome:</label>
                <input type="text" name="nome" value="<%= e.getNome()%>" required>

                <label>Tipo:</label>
                <input type="text" name="tipo" value="<%= e.getTipo()%>" required>

                <label>Espécie destinada:</label>
                <input type="text" name="especie" value="<%= e.getEspecieDestinada()%>" required>

                <label>Frequência:</label>
                <input type="text" name="frequencia" value="<%= e.getFrequencia()%>" required>

                <label for="habitats">Habitat(s) vinculado(s) <small>(selecione pelo menos 1)</small>:</label>
                <select id="habitats" name="habitats" multiple size="5" required>
                    <%
                        if (habitats != null) {
                            List<String> vinculados = e.getHabitats(); 

                            for (String h : habitats) {
                                boolean selecionado = vinculados != null && vinculados.contains(h);
                    %>
                    <option value="<%= h%>" <%= selecionado ? "selected" : ""%> >
                        <%= h%>
                    </option>
                    <%
                        }
                    } else {
                    %>
                    <option disabled>Nenhum habitat cadastrado</option>
                    <% }%>
                </select>

                <label>Observações:</label>
                <textarea name="observacoes"><%= e.getObservacoes()%></textarea>

                <button type="submit" class="btn">Salvar</button>
                <a href="${pageContext.request.contextPath}/enriquecimento?action=ver&id=<%= e.getId()%>" class="btn">Cancelar</a>
            </form>
        </div>

    </body>
</html>
