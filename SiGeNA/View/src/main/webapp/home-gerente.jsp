<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="sigena.model.domain.Cargo" %>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    Cargo cargo = (Cargo) sessao.getAttribute("cargoUsuario");
    if (cargo == null || cargo != Cargo.GERENTE) {
        response.sendRedirect("home.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="pt-br">
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
        <h1>Bem-vindo, <%= sessao.getAttribute("CpfLogado")%>!</h1>

        <div class="grid-botoes">
            <a href="AnimalController?acao=listar" class="btn">Gestão de Animais</a>
            <a href="HabitatController" class="btn">Gestão de Habitat</a>
            <a href="EspeciesController" class="btn">Gestão de Espécies</a>
            <a href="FuncionarioServlet" class="btn">Gestão de Funcionários</a>
            <a href="PlanosAlimentaresController" class="btn">Gestão de Planos Alimentares</a>
            <a href="tratamentos.jsp" class="btn">Gestão de Tratamentos Medicos</a>
        </div>
    </body>
</html>
