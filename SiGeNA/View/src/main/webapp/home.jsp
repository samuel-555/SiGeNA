<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>\

<!DOCTYPE html>
<html lang="pt-br";
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="CSS\style.css">
        <link rel="stylesheet" href="CSS\stylehome.css">
        <title>SiGeNA</title>
    </head>
    <body>
        <header>
            <div class="titulo">SiGeNA</div>
        </header>
        <h1>Bem-vindo, <%= sessao.getAttribute("CpfLogado") %>!</h1>
        <div class="grid-botoes">
            <a href="AnimalController?acao=listar" class="btn">Gestão de Animais</a>
            <a href="habitats.jsp" class="btn">Gestão de Habitat</a>
            <a href="EspeciesController" class="btn">Gestão de Espécies</a>
        </div>
    </body>
</html>
