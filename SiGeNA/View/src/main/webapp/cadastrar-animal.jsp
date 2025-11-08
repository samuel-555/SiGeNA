<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@taglib uri="jakarta.tags.functions" prefix="fn" %>
<%@taglib uri="jakarta.tags.xml" prefix="x" %>
<%@taglib uri="jakarta.tags.sql" prefix="sql"%>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    
    LocalDate data = LocalDate.now();
    String hoje = data.toString();
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
            <a href="AnimalController?acao=listar" class="btn">Voltar</a>
        </div>
        
        <div class="container">
            <h1>Cadastrar Novo Animal</h1>
        <div class="formulario">
            <form action="AnimalController" method="post">
                <label for="nome">Nome do Animal:</label>
                <input type="text" id="nome" name="nome" placeholder="Ex: Simba" required>
                
                <label for="sexo">Sexo do animal:</label>
                <select name="sexo" id="sexo">
                    <option value="indefinido">Indefinido</option>
                    <option value="macho">Macho</option>
                    <option value="femea">Fêmea</option>
                </select>
                
                <label for="dataDeNascimento">Data de nascimento:</label>
                <input type="date" max="<%=hoje%>" id="dataDeNascimento" id="dataDeNascimento" name="dataDeNascimento" required>

                <label for="peso">Peso (kg):</label>
                <input type="number" id="peso" name="peso" min="0" step="0.1" placeholder="Ex: 190.5" required>
                
                <label for="hostil">Animal hostil
                <input type="checkbox" id="hostil" name="hostil" value="true">
                </label>
                
                <input type="hidden" name="acao" value="salvar">
                <button type="submit" class="btn-enviar">Salvar Animal</button>
            </form>
        </div>
        </div>
    </body>
</html>