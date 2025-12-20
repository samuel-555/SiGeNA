<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
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
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SiGeNA - Gestão de Eventos</title>
  <link rel="stylesheet" href="CSS\style.css">
  <link rel="stylesheet" href="CSS\styleanimais.css">
</head>
<body>
  <header>
    <div class="titulo"><a href="<%= request.getContextPath() + ("GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "/home-gerente.jsp" : "/home.jsp") %>">SiGeNA</a></div>
  </header>

  <div class="container">
    <h1>Gestão de Eventos</h1>
    <div class="botoes-acoes">
        <a href="EventoController?acao=cadastrar" class="btn">Cadastrar Novo Evento</a>
    </div>

    <c:if test="${not empty sessionScope.acaoBemSucedida}">
        <p class="sucesso"><c:out value="${sessionScope.acaoBemSucedida}"/></p>
        <c:remove var="acaoBemSucedida" scope="session"/>
    </c:if>
    
        <div class="historico">
        <h2>Lista de 
        <c:choose>
        <c:when test="${param.tipo == 'ocorridos'}">Eventos Ocorridos</c:when>
        <c:when test="${param.tipo == 'cancelados'}">Eventos Cancelados</c:when>
        <c:otherwise>Eventos Ativos</c:otherwise>
        </c:choose>
        </h2>
        <div class="botoes-acoes">
        <c:if test="${not empty param.tipo}">
          <a href="EventoController?acao=listar" class="btn">Ativos</a>
        </c:if>

        <c:if test="${param.tipo != 'ocorridos'}">
          <a href="EventoController?acao=listar&tipo=ocorridos" class="btn">Ocorridos</a>
        </c:if>

        <c:if test="${param.tipo != 'cancelados'}">
         <a href="EventoController?acao=listar&tipo=cancelados" class="btn">Cancelados</a>
        </c:if>
        </div>

        <c:if test="${not empty eventos}">
            <c:forEach var="evento" items="${eventos}">
                <div class="evento">
                  <h2><c:out value="${evento.dataProgramadaFormat}"/> - <c:out value="${evento.horaProgramadaFormat}"/></h2>
                  <h3><c:out value="${evento.titulo}"/></h3>
                  <p><c:out value="${evento.descricao}"/></p>

                  <c:if test="${empty param.tipo}">
                  <form action="EventoController" method="post">
                  <input type="hidden" name="acao" value="excluir">
                        <input type="hidden" name="id" value="<c:out value="${evento.id}"/>">
                        <button type="submit" class="btn-pequeno excluir">Remover</button>
                  </form>
                  <form action="EventoController" method="post">
                        <input type="hidden" name="acao" value="cancelar">
                        <input type="hidden" name="id" value="<c:out value="${evento.id}"/>">
                        <button type="submit" class="btn-pequeno excluir">Cancelar</button>
                  </form>
                  </c:if>
                  
                  <c:if test="${param.tipo == 'cancelados'}">
                  <form action="EventoController" method="post">
                        <input type="hidden" name="acao" value="ativar">
                        <input type="hidden" name="id" value="<c:out value="${evento.id}"/>">
                        <button type="submit" class="btn-pequeno excluir">Ativar</button>
                  </form>
                  </c:if>
                  <a href="EventoController?acao=exibir&id=<c:out value="${evento.id}"/>" class="btn-pequeno">Exibir</a>
                </div>
            </c:forEach>
    </c:if>
    <c:if test="${empty eventos}">
      <p>Nenhum evento encontrado.</p>
    </c:if>
    
  </div>
</body>
</html>

