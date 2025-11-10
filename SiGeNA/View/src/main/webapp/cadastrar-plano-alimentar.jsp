<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SiGeNA - Cadastrar Plano Alimentar</title>
        <link rel="stylesheet" href="CSS/styleplanos.css">
        <link rel="stylesheet" href="CSS/style.css">
    </head>
    <body>
        <header>
            <div class="titulo"><a href="home.jsp">SiGeNA</a></div>
        </header>

        <div class="botoes-acoes">
            <a href="PlanosAlimentaresController" class="btn">Voltar</a>
            <a href="home.jsp" class="btn">Home</a>
        </div>

        <div class="container">
            <h1>Cadastrar Plano Alimentar</h1>
        <div class="formulario">
            <form action="PlanosAlimentaresController" method="post">
                <label for="animal">Animal:</label>
                <select name="animal_id" id="animal" required>
                    <option value="">Selecione um animal</option>
                    <c:forEach items="${animais}" var="animal">
                        <option value="${animal.id}">${animal.nome}</option>
                    </c:forEach>
                </select>

                <h2>Itens do Plano</h2>
                <div id="itens-container">
                    <div class="item-row">
                        <label>Alimento:</label>
                        <input type="text" name="alimento" placeholder="Ex: Carne, Ração">
                        <label>Gramatura (g):</label>
                        <input type="number" name="gramatura" min="0" step="0.1" placeholder="Ex: 250">
                        <label>Vezes/dia:</label>
                        <input type="number" name="vezes_por_dia" min="1" step="1" placeholder="Ex: 2">
                    </div>
                </div>
                <button type="button" class="btn" onclick="addItem()">Adicionar Alimento</button>

                <c:if test="${not empty erro}">
                    <div class="mensagem"><c:out value="${erro}"/></div>
                </c:if>

                <input type="hidden" name="acao" value="salvar">
                <button type="submit" class="btn-enviar">Salvar Plano</button>
            </form>
            <script>
                function addItem(){
                    const container = document.getElementById('itens-container');
                    const row = document.createElement('div');
                    row.className = 'item-row';
                    row.innerHTML = `
                        <label>Alimento:</label>
                        <input type="text" name="alimento" placeholder="Ex: Carne, Ração">
                        <label>Gramatura (g):</label>
                        <input type="number" name="gramatura" min="0" step="0.1" placeholder="Ex: 250">
                        <label>Vezes/dia:</label>
                        <input type="number" name="vezes_por_dia" min="1" step="1" placeholder="Ex: 2">
                    `;
                    container.appendChild(row);
                }
            </script>
        </div>
        </div>
    </body>
</html>