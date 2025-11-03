<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
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
            <form action="AnimalController.java" method="post">
                <label for="nome">Nome do Animal:</label>
                <input type="text" id="nome" name="nome" placeholder="Ex: Simba" required>

                <label for="especie">Espécie:</label>
                <input type="text" id="especie" name="especie" placeholder="Ex: Leão Africano" required>

                <label for="idade">Idade (anos):</label>
                <input type="number" id="idade" name="idade" min="0" placeholder="Ex: 4" required>

                <label for="peso">Peso (kg):</label>
                <input type="number" id="peso" name="peso" min="0" step="0.1" placeholder="Ex: 190.5" required>

                <label for="habitat">Habitat:</label>
                <input type="text" id="habitat" name="habitat" placeholder="Ex: Savana 3" required>
                
                <input type="hidden" name="acao" value="salvar">
                <button type="submit" class="btn-enviar">Salvar Animal</button>
            </form>
        </div>
        </div>
    </body>
</html>