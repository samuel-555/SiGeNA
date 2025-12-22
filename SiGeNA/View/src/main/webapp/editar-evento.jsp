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
        <link rel="stylesheet" href="CSS/stylehome.css">
    <link rel="stylesheet" href="CSS/stylefuncionalidades.css">
    </head>
    <body>
        <header class="topbar">
            <a href="HomeController" class="titulo">
                <img src="IMG's/logoSiGeNA-COR2.png" alt="Logo" class="brand-logo">
                <span>SiGeNA</span>
            </a>
            <div class="user-area">
                <a href="LogoutServlet" class="btn-sair">Sair</a>
            </div>
        </header>
        
        <div class="botoes-acoes">
            <a href="EventoController?acao=exibir&id=${evento.id}" class="btn">Voltar</a>
        </div>
        
        <div class="container">
            <h1>Editar Evento</h1>
        <div class="formulario">
            <form action="EventoController" method="post">
                <label for="nome">Título do evento:*</label>
                <input type="text" id="titulo" name="titulo" class="obrigatorio" value="<c:out value="${evento.titulo}"/>" required>
                
                <label for="descricao">Descrição:</label>
                <textarea name="descricao"><c:out value="${evento.descricao}"/></textarea>

                <label>Data e horário previstos:*<input type="datetime-local" name="data-programada" class="obrigatorio" id="data-programada" value="<c:out value="${evento.dataProgramada}"/>" required></label>
                
                <input type="hidden" name="acao" value="editar">
                <input type="hidden" name="id" value="<c:out value="${evento.id}"/>">
                <button type="submit" class="btn-enviar" onclick="return confirm('Salvar alterações? Essas modificações não poderão ser desfeitas.')">Salvar Alterações</button>
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
        <script src="JS/verificar-campos.js"></script>
    </body>
</html>
