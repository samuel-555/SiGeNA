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
            <a href="TarefaController" class="titulo">
                <img src="IMG's/logoSiGeNA-COR2.png" alt="Logo" class="brand-logo">
                <span>SiGeNA</span>
            </a>
            <div class="user-area">
                <a href="LogoutServlet" class="btn-sair">Sair</a>
            </div>
        </header>
        
        <div class="container">
            <h1>Cadastrar Novo Evento</h1>
            
            <a href="EventoController?acao=listar" class="btn-sair" style="background: var(--zoo-mint); color: var(--zoo-dark-green); margin-right: 10px;">Voltar</a>
        <div class="formulario">
            <form action="EventoController" method="post">
                <label for="nome">Título do evento:*</label>
                <input type="text" id="titulo" name="titulo" class="obrigatorio" required>
                
                <label for="descricao">Descrição:</label>
                <textarea name="descricao"></textarea>

                <label>Data e horário previstos:*<input type="datetime-local" name="data-programada" id="data-programada" class="obrigatorio" required></label>
                
                <c:if test="${not empty sessionScope.campoInvalidoErro}">
                    <div class="mensagem"><c:out value="${sessionScope.campoInvalidoErro}"/></div>
                    <c:remove var="campoInvalidoErro" scope="session"/>
                </c:if>
                
                <input type="hidden" name="acao" value="salvar">
                <button type="submit" class="btn-enviar" onclick="return confirm('Salvar evento?')">Salvar Evento</button>
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
