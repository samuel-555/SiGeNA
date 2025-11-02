<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SiGeNA - Gestão de Animais</title>
        <link rel="stylesheet" href="CSS\styleanimais.css">
        <link rel="stylesheet" href="CSS\style.css">
    </head>
    <body>
        <header>
            <div class="titulo"><a href="home.jsp">SiGeNA</a></div>
        </header>
        
        <div class="botoes-acoes">
            <a href="animais.jsp" class="btn">Listar animais</a>
        </div>
        
        <div class="container">
            <h1>Cadastrar Novo Animal</h1>
        <div class="formulario">
            <form action="">
                <label for="nome">Nome do Animal:</label>
                <input type="text" id="nome" placeholder="Ex: Simba">

                <label for="especie">Espécie:</label>
                <input type="text" id="especie" placeholder="Ex: Leão Africano">

                <label for="idade">Idade (anos):</label>
                <input type="number" id="idade" min="0" placeholder="Ex: 4">

                <label for="peso">Peso (kg):</label>
                <input type="number" id="peso" min="0" step="0.1" placeholder="Ex: 190.5">

                <label for="habitat">Habitat:</label>
                <input type="text" id="habitat" placeholder="Ex: Savana 3">

                <button type="submit" class="btn-enviar">Salvar Animal</button>
            </form>
        </div>
        </div>
    </body>
</html>
