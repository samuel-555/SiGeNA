<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SiGeNA - Plano Alimentar</title>
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
            <h1>Detalhes do Plano Alimentar</h1>
            <div class="historico">
                <h2>Informações</h2>
                <p><strong>Animal:</strong> ${plano.animal.nome}</p>
                <h3>Itens do Plano</h3>
                <ul>
                    <c:forEach items="${plano.itens}" var="it">
                        <li>
                            <strong>${it.alimento}</strong> - ${it.gramatura} g, ${it.vezesPorDia}x/dia
                        </li>
                    </c:forEach>
                    <c:if test="${empty plano.itens}">
                        <li>Nenhum alimento cadastrado.</li>
                    </c:if>
                </ul>

                <a href="PlanosAlimentaresController?acao=editar&id=${plano.id}" class="btn">Editar</a>
            </div>
        </div>
    </body>
    </html>