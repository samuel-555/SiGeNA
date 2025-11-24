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
  <title>SiGeNA - Gestão de Animais</title>
  <link rel="stylesheet" href="CSS\styleanimais.css">
  <link rel="stylesheet" href="CSS\style.css">
</head>
<body>
  <header>
    <div class="titulo"><a href="<%= request.getContextPath() + ("GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "/home-gerente.jsp" : "/home.jsp") %>">SiGeNA</a></div>
  </header>

  <div class="container">
    <h1>Gestão de Fornecedores</h1>

    <div class="botoes-acoes">
        <a href="FornecedorController?acao=cadastrar" class="btn">Cadastrar Novo Fornecedor</a>
    </div>
    <c:if test="${empty fornecedores}">
        <p>Nenhum fornecedor cadastrado.</p>
    </c:if>
    <c:if test="${not empty sessionScope.acaoBemSucedida}">
        <p class="sucesso"><c:out value="${sessionScope.acaoBemSucedida}"/></p>
        <c:remove var="acaoBemSucedida" scope="session"/>
    </c:if>
    <c:if test="${not empty fornecedores}">
        <div class="historico">
        <h2>Lista de Fornecedores</h2>
        <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Tipo</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
            <c:forEach var="fornecedor" items="${fornecedores}">
                <tr>
                <td><c:out value="${fornecedor.id}"/></td>
                <td><c:out value="${fornecedor.nome}"/></td>
                <td><c:out value="${fornecedor.tipo}"/></td>
                <td>
                    <form action="AnimalController" method="post" class="botao-acao">
                        <input type="hidden" name="acao" value="excluir">
                        <input type="hidden" name="id" value="<c:out value="${fornecedor.id}"/>">
                        <button type="submit" class="btn-pequeno excluir">Remover</button>
                    </form>
                        <a href="AnimalController?acao=exibir&id=<c:out value="${fornecedor.id}"/>" class="btn-pequeno">Exibir</a>
                    
                </td>
                </tr>
            </c:forEach>
          
        </tbody>
      </table>
    </div>     
    </c:if>
    
  </div>
</body>
</html>
