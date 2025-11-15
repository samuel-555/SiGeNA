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
            <a href="HabitatController?acao=listar" class="btn">Voltar</a>
        </div>
        
        <div class="container">
            <h1>Editar Habitat</h1>
        <div class="formulario">
            <form action="${pageContext.request.contextPath}/HabitatController" method="post">
                <input type="hidden" name="acao" value="editar">
                <input type="hidden" name="nomeAntigo" value="${habitat.nome}"/>                

                <label for="nome">Nome do Habitat:</label>
                <input type="text" id="nome" name="nome" placeholder="Savana doida" value="${habitat.nome}" required>

                <label for="tipo">Tipo de Habitat:</label>
                <input type="text" id="tipo" name="tipo" placeholder="Terra" value="${habitat.tipo}" required>

                <label for="tamanho">Tamanho em m³:</label>
                <input type="number" id="tamanho" name="tamanho" placeholder="10 m³" value="${habitat.tamanho}" min="1" required>

                <label for="manutencao">Precisa de Manutenção</label>
                <input type="checkbox" id="manutencao" name="manutencao" <c:if test="${habitat.manutencao}">checked</c:if> >

                <button type="submit" class="btn-enviar">Salvar Alterações</button>
            </form>


        </div>
        </div>
    </body>
</html>