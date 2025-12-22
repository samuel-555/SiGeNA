<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>SiGeNA - Cadastrar Enriquecimento</title>
        <link rel="stylesheet" href="CSS/styleespecies.css">
        <link rel="stylesheet" href="CSS\style.css">
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
            <div class="formulario">
                <h2>Adicionar Novo Enriquecimento</h2>
                <a href="enriquecimento" class="btn-sair" style="background: var(--zoo-mint); color: var(--zoo-dark-green); margin-right: 10px;">Voltar</a>
                <c:if test="${not empty erro}">
                    <div style="color:#c0392b; margin-bottom:10px;">${erro}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/enriquecimento" method="post">
                    <label for="nome">Nome do Enriquecimento:</label>
                    <input type="text" id="nome" name="nome" placeholder="Ex: Bola com petiscos" required>

                    <label for="tipo">Tipo:</label>
                    <input type="text" id="tipo" name="tipo" placeholder="Ex: Alimentar, Cognitivo, Sensorial" required>

                    <label for="especie">Espécie Destinada:</label>
                    <input type="text" id="especie" name="especie" placeholder="Ex: Felino, Primata">

                    <label for="frequencia">Frequência de Uso:</label>
                    <input type="text" id="frequencia" name="frequencia" placeholder="Ex: Diário, Semanal">

                    <label for="habitats">Habitat(s) vinculado(s) <small>(selecione pelo menos 1)</small>:</label>
                    <select id="habitats" name="habitats" multiple size="5" required>
                        <%
                            java.util.List<String> habitats = (java.util.List<String>) request.getAttribute("habitats");
                            if (habitats != null) {
                                for (String h : habitats) {
                        %>
                            <option value="<%= h %>"><%= h %></option>
                        <%
                                }
                            } else {
                        %>
                            <option disabled> Nenhum habitat cadastrado </option>
                        <%
                            }
                        %>
                    </select>

                    <label for="observacoes">Observações:</label>
                    <textarea id="observacoes" name="observacoes" rows="3" placeholder="Ex: Limpar antes de cada uso"></textarea>

                    <button type="submit" class="btn-enviar">Salvar Enriquecimento</button>
                </form>
            </div>
        </div>
    </body>
</html>
