<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ include file="/WEB-INF/jspf/permissoes.jspf" %>
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
    Cargo cargo = (Cargo) sessao.getAttribute("cargoUsuario");
    boolean podeCadastrar = temPermissaoCadastro(cargo, "animais");
    boolean podeGerenciar = temPermissaoGerenciamento(cargo, "animais");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SiGeNA - Gestão de Animais</title>
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
    <h1>Gestão de Animais</h1>
    <c:if test="${empty especies || empty habitats}">
        <p>Para cadastrar animais, deverá ser feito o cadastro de, no mínimo, 1 espécie e 1 habitat previamente</p>
    </c:if>
    <c:if test="${not empty especies && not empty habitats}">
    <div class="botoes-acoes">
        <% if (podeCadastrar) { %>
        <a href="AnimalController?acao=cadastrar" class="btn">Cadastrar Novo Animal</a>
        <% } %>
    </div>
    <div class="pesquisa">
          Pesquaisar: <input type="text" placeholder="Digite o ID ou o nome"><br>
          Filtrar espécie: <select class="filtro">
                    <option value="">Todas</option>
                    <c:forEach items="${especies}" var="especie">
                        <option value="${especie.id}">${especie.nome}</option>
                    </c:forEach>
                </select>
          
          Ordenar por: <select class="sequencia">
            <option value="adicionado" data-ordem="crescente">Adicionado recentemente</option>
            <option value="adicionado" data-ordem="decrescente">Mais antigo</option>
            <option value="alfabetica" data-ordem="crescente">Alfabética A-Z</option>
            <option value="alfabetica" data-ordem="decrescente">Alfabética Z-A</option>
          </select>

    </div>
    <c:if test="${empty animais}">
        <p>Nenhum animal encontrado.</p>
    </c:if>
    <c:if test="${not empty sessionScope.acaoBemSucedida}">
        <p class="sucesso"><c:out value="${sessionScope.acaoBemSucedida}"/></p>
        <c:remove var="acaoBemSucedida" scope="session"/>
    </c:if>
    <c:if test="${not empty animais}">
        <div class="historico">
        <h2>Lista de Animais</h2>

        <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Espécie</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
            <c:forEach var="animal" items="${animais}">
                <tr>
                <td><c:out value="${animal.id}"/></td>
                <td><c:out value="${animal.nome}"/></td>
                <td><c:out value="${animal.especieNome}"/></td>
                <td>
                    <% if (podeGerenciar) { %>
                    <form action="AnimalController" method="post" class="botao-acao">
                        <input type="hidden" name="acao" value="excluir">
                        <input type="hidden" name="id" value="<c:out value="${animal.id}"/>">
                        <button type="submit" class="btn-pequeno excluir" onclick="return confirm('Tem certeza que deseja excluir o animal ${animal.nome} permanentemente?')">Excluir</button>
                    </form>
                    <% } %>
                    <a href="AnimalController?acao=exibir&id=<c:out value="${animal.id}"/>" class="btn-pequeno">Exibir</a>
                    
                </td>
                </tr>
            </c:forEach>
          
        </tbody>
      </table>
    </div>     
    </c:if>
    </c:if>
    
  </div>

  <script src="JS/pesquisa.js">
  </script>
</body>
</html>
