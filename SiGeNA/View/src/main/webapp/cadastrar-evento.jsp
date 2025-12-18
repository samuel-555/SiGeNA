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
        <title>SiGeNA - Gestão de Eventos</title>
        <link rel="stylesheet" href="CSS\styleanimais.css">
        <link rel="stylesheet" href="CSS\style.css">
    </head>
    <body>
        <header>
            <div class="titulo"><a href="<%= request.getContextPath() + ("GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "/home-gerente.jsp" : "/home.jsp") %>">SiGeNA</a></div>
        </header>
        
        <div class="botoes-acoes">
            <a href="EventoController?acao=listar" class="btn">Voltar</a>
        </div>
        
        <div class="container">
            <h1>Cadastrar Novo Evento</h1>
        <div class="formulario">
            <form action="EventoController" method="post">
                <label for="nome">Título do evento:*</label>
                <input type="text" id="titulo" name="titulo" required>
                
                <label for="descricao">Descrição:</label>
                <textarea name="descricao"></textarea>

                <label>Data e horário previstos:*<input type="datetime-local" name="data-programada" required></label>
                
                <input type="hidden" name="acao" value="salvar">
                <button type="submit" class="btn-enviar">Salvar Evento</button>
            </form>
        </div>
        </div>
    </body>
</html>
