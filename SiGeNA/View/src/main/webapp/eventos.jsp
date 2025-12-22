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
    <h1>Gestão de Eventos</h1>
    <div class="botoes-acoes">
        <a href="EventoController?acao=cadastrar" class="btn">Cadastrar Novo Evento</a>
    </div>
    <form method="get" action="EventoController">
      <input type="hidden" name="acao" value="listar">
      <input type="hidden" name="tipo" value="${param.tipo}">

      Período:<br>
      <label>Data inicial:</label>
      <input type="datetime-local" name="dataInicio" value="${dataInicio}">

      <label>Data final:</label>
      <input type="datetime-local" name="dataFim" value="${dataFim}">

      <button type="submit" class="btn">Filtrar</button>
    </form>
    <c:if test="${not empty sessionScope.acaoBemSucedida}">
        <p class="sucesso"><c:out value="${sessionScope.acaoBemSucedida}"/></p>
        <c:remove var="acaoBemSucedida" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.erro}">
        <div class="mensagem"><c:out value="${sessionScope.erro}"/></div>
        <c:remove var="erro" scope="session"/>
    </c:if>
    <div class="pesquisa">
          Pesquaisar: <input type="text" placeholder="Digite o título do evento"><br>
          
          <c:if test="${param.tipo != 'ocorridos'}">
            Ordenar por: <select class="sequencia">
              <option value="adicionado" data-ordem="crescente">Data mais próxima</option>
              <option value="adicionado" data-ordem="decrescente">Data mais distante</option>
            </select>
          </c:if>

          <c:if test="${param.tipo == 'ocorridos'}">
            <select class="sequencia">
            <option value="adicionado" data-ordem="decrescente">Data mais recente</option>
            <option value="adicionado" data-ordem="crescente">Data mais antiga</option>
          </select>
            Status: <select class="filtro">
              <option value="">Todos</option>
              <option value="ocorridos">Ocorrido</option>
              <option value="cancelados">Cancelado</option>
            </select>
          </c:if>

    </div>
    
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
          <a href="EventoController?acao=listar&tipo=ocorridos" class="btn">Histórico</a>
        </c:if>

        <c:if test="${param.tipo != 'cancelados'}">
         <a href="EventoController?acao=listar&tipo=cancelados" class="btn">Cancelados</a>
        </c:if>
        </div>

        <c:if test="${not empty eventos}">
            <c:forEach var="evento" items="${eventos}">
                <div class="evento">
                  <h2><c:out value="${evento.dataProgramadaFormat}"/> - <c:out value="${evento.horaProgramadaFormat}"/></h2>
                  <h3><c:out value="${evento.titulo}"/>
                  <c:choose>
                    <c:when test="${evento.ocorrido}">
                      - Ocorrido
                    </c:when>

                    <c:when test="${evento.cancelado}">
                      - Cancelado
                    </c:when>
                  </c:choose>
                  </h3>

                  <c:if test="${empty param.tipo}">
                  <form action="EventoController" method="post">
                  <input type="hidden" name="acao" value="excluir">
                        <input type="hidden" name="id" value="<c:out value="${evento.id}"/>">
                        <button type="submit" class="btn-pequeno excluir" onclick="return confirm('Tem certeza que deseja excluir o evento marcado para ${evento.dataProgramadaFormat} às ${evento.horaProgramadaFormat} permanentemente?')">Excluir</button>
                  </form>
                  <form action="EventoController" method="post">
                        <input type="hidden" name="acao" value="cancelar">
                        <input type="hidden" name="id" value="<c:out value="${evento.id}"/>">
                        <button type="submit" class="btn-pequeno excluir" onclick="return confirm('Tem certeza que deseja cancelar o evento marcado para ${evento.dataProgramadaFormat} às ${evento.horaProgramadaFormat}? Você poderá reativá-lo antes de seu prazo terminar.')">Cancelar</button>
                  </form>
                  </c:if>
                  
                  <c:if test="${param.tipo == 'cancelados' and not expirado}">
                  <form action="EventoController" method="post">
                        <input type="hidden" name="acao" value="ativar">
                        <input type="hidden" name="id" value="<c:out value="${evento.id}"/>">
                        <button type="submit" class="btn-pequeno excluir" onclick="return confirm('Tem certeza que deseja reativar o evento marcado para ${evento.dataProgramadaFormat} às ${evento.horaProgramadaFormat}?')">Ativar</button>
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
  <c:choose>
        <c:when test="${param.tipo == 'ocorridos'}"><script src="JS/pesquisa-evento.js"></script></c:when>
        <c:otherwise><script src="JS/pesquisa-por-nome-sequencia.js"></script></c:otherwise>
        </c:choose>
</body>
</html>

