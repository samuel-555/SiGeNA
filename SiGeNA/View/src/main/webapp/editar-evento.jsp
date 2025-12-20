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
            <a href="EventoController?acao=exibir&id=${evento.id}" class="btn">Voltar</a>
        </div>
        
        <div class="container">
            <h1>Editar Evento</h1>
        <div class="formulario">
            <form action="EventoController" method="post">
                <label for="nome">Título do evento:*</label>
                <input type="text" id="titulo" name="titulo" value="<c:out value="${evento.titulo}"/>" required>
                
                <label for="descricao">Descrição:</label>
                <textarea name="descricao"><c:out value="${evento.descricao}"/></textarea>

                <label>Data e horário previstos:*<input type="datetime-local" name="data-programada" id="data-programada" value="<c:out value="${evento.dataProgramada}"/>" required></label>
                
                <input type="hidden" name="acao" value="editar">
                <input type="hidden" name="id" value="<c:out value="${evento.id}"/>">
                <button type="submit" class="btn-enviar">Salvar Alterações</button>
            </form>
        </div>
        </div>
        
        <script>
            const input = document.getElementById("data-programada");

            const agora = new Date();
            agora.setMinutes(agora.getMinutes() - agora.getTimezoneOffset());
            agora.setSeconds(0, 0);

            input.min = agora.toISOString().slice(0, 16);
        </script>
    </body>
</html>
