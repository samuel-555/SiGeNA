<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>SiGeNA - Gestão de Espécies</title>
        <link rel="stylesheet" href="CSS/styleespecies.css">
    </head>
    <body>
        <header>
            <div class="titulo"><a href="<%= request.getContextPath() + ("GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "/home-gerente.jsp" : "/home.jsp") %>">SiGeNA</a></div>
        </header>

        <div class="container">
            <h1>Gestão de Espécies</h1>
            <div class="botoes-acoes">
                <a href="EspeciesController" class="btn">Voltar para Catálogo</a>
            </div>

            <div class="formulario">
                <h2>Adicionar Nova Espécie</h2>
                <form action="EspeciesController" method="post">
                    <label for="nome">Nome da Espécie:</label>
                    <input type="text" id="nome" name="nome" placeholder="Ex: Panthera leo">

                    <label for="classe">Classe:</label>
                    <input type="text" id="classe" name="classe" placeholder="Ex: Mamífero, Réptil">

                    <label for="habitat">Habitat Natural:</label>
                    <input type="text" id="habitat" name="habitat" placeholder="Ex: Savana, Floresta Tropical">

                    <label for="alimentacao">Alimentação:</label>
                    <input type="text" id="alimentacao" name="alimentacao" placeholder="Ex: Carnívoro, Herbívoro">

                    <div class="checkbox-group">
                        <input type="checkbox" id="predador" name="predador" value="true">
                        <label for="predador">Espécie Predadora</label>
                    </div>


                    <label for="observacoes">Observações:</label>
                    <textarea id="observacoes" name="observacoes" rows="3" placeholder="Ex: Necessita de grande área para exercícios"></textarea>

                    <button type="submit" class="btn-enviar">Salvar Espécie</button>
                </form>
            </div>

        </div>
    </body>
</html>
