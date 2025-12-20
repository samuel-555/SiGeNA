<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@taglib uri="jakarta.tags.core" prefix="c" %>

<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>SiGeNA - Gestão de Eventos</title>
    <link rel="stylesheet" href="CSS/style.css">
    <link rel="stylesheet" href="CSS\styleanimais.css">
</head>
<body>

<header>
    <div class="titulo">
        <a href="<%= request.getContextPath() + ("GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "/home-gerente.jsp" : "/home.jsp") %>">
            SiGeNA
        </a>
    </div>
</header>

<div class="container">
    <h1>Detalhes do Evento</h1>
    
    <div class="botoes-acoes">
        <c:choose>
            <c:when test="${evento.cancelado}">
                <a href="EventoController?acao=listar&tipo=cancelados" class="btn">Voltar</a>
            </c:when>
            <c:when test="${evento.ocorrido}">
                <a href="EventoController?acao=listar&tipo=ocorridos" class="btn">Voltar</a>
            </c:when>
            <c:otherwise>
                <a href="EventoController?acao=listar" class="btn">Voltar</a>
            </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${empty evento}">
        <p class="erro">Erro: Evento não encontrado</p>
    </c:if>

    <c:if test="${not empty sessionScope.acaoBemSucedida}">
        <p class="sucesso">
            <c:out value="${sessionScope.acaoBemSucedida}"/>
        </p>
        <c:remove var="acaoBemSucedida" scope="session"/>
    </c:if>

    <c:if test="${not empty evento}">
        <div class="ficha-evento">
            <h3><c:out value="${evento.titulo}"/></h3>

            <p><strong>Descrição:</strong><br>
                <c:out value="${evento.descricao}"/>
            </p>

            <p>
                <strong>Data:</strong>
                <c:out value="${evento.dataProgramadaFormat}"/>
                às
                <c:out value="${evento.horaProgramadaFormat}"/>
            </p>

            <p>
                <strong>Status:</strong>
                <c:choose>
                    <c:when test="${evento.cancelado}">
                        <span class="status cancelado">Cancelado</span>
                    </c:when>
                    <c:when test="${evento.ocorrido}">
                        <span class="status ocorrido">Ocorrido</span>
                    </c:when>
                    <c:otherwise>
                        <span class="status ativo">Ativo</span>
                    </c:otherwise>
                </c:choose>
            </p>
        </div>

        <div class="botoes-acoes">

            <c:if test="${not evento.cancelado and not evento.ocorrido}">
                <a href="EventoController?acao=editar&id=${evento.id}" class="btn">Editar</a>

                <form action="EventoController" method="post" style="display:inline">
                    <input type="hidden" name="acao" value="cancelar">
                    <input type="hidden" name="id" value="${evento.id}">
                    <button type="submit" class="btn cancelar">Cancelar</button>
                </form>
            </c:if>

            <c:if test="${evento.cancelado}">
                <form action="EventoController" method="post" style="display:inline">
                    <input type="hidden" name="acao" value="ativar">
                    <input type="hidden" name="id" value="${evento.id}">
                    <button type="submit" class="btn">Reativar</button>
                </form>
            </c:if>

        </div>

    </c:if>

</div>

</body>
</html>

