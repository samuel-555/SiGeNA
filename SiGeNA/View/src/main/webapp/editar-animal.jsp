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
        <title>SiGeNA - Gestão de Animais</title>
        <link rel="stylesheet" href="CSS/styleanimais.css">
        <link rel="stylesheet" href="CSS/style.css">
    </head>
    <body>
        <header>
            <div class="titulo"><a href="<%= request.getContextPath() + ("GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "/home-gerente.jsp" : "/home.jsp") %>">SiGeNA</a></div>
        </header>
        
        <div class="botoes-acoes">
            <a href="AnimalController?acao=exibir&id=<c:out value="${animal.id}"/>" class="btn">Voltar</a>
        </div>
        
        <div class="container">
            <h1>Editar Animal</h1>
        <div class="formulario">
            <p><strong>ID:</strong> <c:out value="${animal.id}"/></p>
            <form action="AnimalController" method="post">
                <label for="nome">Nome do Animal:*</label>
                <input type="text" id="nome" name="nome" class="obrigatorio" value="<c:out value="${animal.nome}"/>" placeholder="Ex: Simba">
                
                <label for="especie">Espécie:*</label>
                <select name="especie" id="especie" class="obrigatorio">
                    <option value="${animal.especieId}">${animal.especieNome}</option>
                    <c:forEach items="${especies}" var="especie">
                        <c:if test="${animal.especieId != especie.id}">
                            <option value="${especie.id}">${especie.nome}</option>
                        </c:if>
                    </c:forEach>
                </select>
                
                <label for="sexo">Sexo do animal:</label>
                <select name="sexo" id="sexo">
                    <option value="Indefinido">Indefinido</option>
                    <option value="Macho">Macho</option>
                    <option value="Fêmea">Fêmea</option>
                </select>
                
                <label for="dataDeNascimento">Data de nascimento:*</label>
                <input type="date" max="<%=hoje%>" id="dataDeNascimento" name="dataDeNascimento" class="obrigatorio" value="<c:out value="${animal.dataDeNascimentoOb}"/>" required>

                <label for="peso">Peso (kg)*:</label>
                <input type="number" id="peso" name="peso" class="obrigatorio" min="0" step="0.1" value="<c:out value="${animal.peso}"/>" placeholder="Ex: 190.5" required>
                
                <div class="checkbox-group">
                    <input type="checkbox" id="hostil" name="hostil" value="true" <c:if test="${animal.hostilidade}">checked</c:if>>
                    <label for="hostil">Animal hostil</label>
                </div>
                
                <label for="habitat">Habitat:*</label>
                <select name="habitat" id="habitat" class="obrigatorio">
                    <option value="${animal.habitatNome}">${animal.habitatNome}</option>
                    <c:forEach items="${habitats}" var="habitat">
                        <c:if test="${animal.habitatNome != habitat.nome}">
                            <option value="${habitat.nome}">${habitat.nome}</option>
                        </c:if>
                    </c:forEach>
                </select><br>
                
                <c:if test="${not empty sessionScope.campoInvalidoErro}">
                    <div class="mensagem"><c:out value="${sessionScope.campoInvalidoErro}"/></div>
                    <c:remove var="campoInvalidoErro" scope="session"/>
                </c:if>
                    
                <input type="hidden" name="id" value="<c:out value="${animal.id}"/>"> 
                <input type="hidden" name="acao" value="editar">
                <button type="submit" class="btn-enviar" onclick="return confirm('Salvar alterações? Essas modificações não poderão ser desfeitas.')">Salvar Alterações</button>
            </form>
        </div>
        </div>
        <script>
            document.querySelector('#sexo').value = <c:out value="${animal.sexo}"/>;
        </script>
        <script src="JS/verificar-campos.js"></script>
    </body>
</html>
