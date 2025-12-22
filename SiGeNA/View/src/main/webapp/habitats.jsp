<%-- 
    Document   : habitats
    Created on : 3 de nov. de 2025, 09:26:00
    Author     : USUARIO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ include file="/WEB-INF/jspf/permissoes.jspf" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>


<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    Cargo cargo = (Cargo) sessao.getAttribute("cargoUsuario");
    boolean podeCadastrar = temPermissaoCadastro(cargo, "habitats");
    boolean podeGerenciar = temPermissaoGerenciamento(cargo, "habitats");
    request.setAttribute("podeGerenciarHabitats", podeGerenciar);
%>


<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SiGeNA - Gestão de Habitat</title>
  <link rel="stylesheet" href="CSS\style.css">
  <link rel="stylesheet" href="CSS\stylehabitat.css">
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
    <h1>Gestão de Habitat</h1>

    <div class="botoes-acoes">
      <% if (podeCadastrar) { %>
      <a href="cadastrar-habitat.jsp" class="btn">Cadastrar Novo Habitat</a>
      <% } %>
    </div>

    
    <div class="lista-de-habitats">
      <h2>Lista de Habitats</h2>
      
    <form id="formBusca"
      action="HabitatController"
      method="get"
      class="form-busca">

        <input type="hidden" name="acao" value="buscar">

        <input type="text"
           name="q"
           placeholder="Buscar por nome ou tipo do habitat"
           value="${param.q}"
           onkeyup="buscar()">
    </form>
           
    <script>
        let timer;

        function buscar() {
            clearTimeout(timer);
            timer = setTimeout(() => {
                document.getElementById("formBusca").submit();
            }, 500);
        }
    </script>
      
    <c:if test="${empty habitats and empty param.q}">
        <p>Nenhum habitat cadastrado.</p>
    </c:if>

    <c:if test="${empty habitats and not empty param.q}">
        <p style="color:#c00; font-weight:bold;">
            Nenhum habitat encontrado para a busca "<c:out value='${param.q}'/>".
        </p>
    </c:if>
        
    <c:if test="${not empty habitats}">
    
    
  <table>
    <thead>
      <tr>
        <th>Nome</th>
        <th>Tipo</th>
        <th>Tamanho</th>
        <th>Precisa de Manutenção</th>
        <th>Disponibilidade</th>
        <th>Ações</th>
      </tr>
    </thead>

    <tbody>
        <c:forEach var="habitat" items="${habitats}">

          <tr>
            <td>${habitat.nome}</td>
            <td>${habitat.tipo}</td>
            <td>${habitat.tamanho}</td>
            <td>${habitat.manutencao ? "Sim" : "Não"}</td>
            <td>${habitat.disponivel ? "Disponível":"Indisponível"}</td>
            <td>
                
              <c:if test="${podeGerenciarHabitats}">
                  <a href="HabitatController?acao=editar&nome=${habitat.nome}" class="btn-pequeno editar">Editar</a>


                  <form action="${pageContext.request.contextPath}/HabitatController" method="POST" style="display:inline-block;">
                    <c:if test="${not empty msgErro}">
                        <p style="color:red;font-weight:bold">${msgErro}</p>
                    </c:if>
                        
                      <input type="hidden" name="acao" value="excluir">
                      <input type="hidden" name="nome" value="${habitat.nome}">
                      <button type="submit" class="btn-pequeno excluir" onclick="return confirm('Deseja realmente excluir este habitat?')">Excluir</button>
                 </form>
              </c:if>
            </td>
          </tr>
        </c:forEach>
        </tbody>
    </table>
    </c:if>

    </div>
  </div>
</body>
</html>
