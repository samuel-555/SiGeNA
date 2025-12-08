
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@page import="sigena.model.domain.Cargo"%>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    
    Cargo cargo = (Cargo) sessao.getAttribute("cargoUsuario");
    
%>

<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="CSS\style.css">
        <link rel="stylesheet" href="CSS\stylehome.css">
        <title>SiGeNA</title>
    </head>
    <body>
        <header>
            <a style="visibility: hidden">.</a>
            <div class="titulo">SiGeNA</div>
            <a href="index.jsp" class="btn-sair">Sair</a>
        </header>
        
        <h1>Bem-vindo, <%= sessao.getAttribute("CpfLogado") %>!</h1>
        <c:set var="cargoUsuario" value="${sessionScope.cargoUsuario}" />

        <div class="grid-botoes">
            <a href="AnimalController?acao=listar" class="btn">Gestão de Animais</a>
            <a href="EspeciesController" class="btn">Gestão de Espécies</a>
            <a href="PlanosAlimentaresController" class="btn">Gestão de Planos Alimentares</a>
            <a href="HabitatController?acao=listar" class="btn">Gestão de Habitat</a>
            <a href="tratamentos.jsp" class="btn">Gestão de Tratamentos Medicos</a>
            <a href="RelatorioSaudeController" class="btn">Gestão de Relatórios de Saúde</a>
            <a href="ProdutoController?acao=listar" class="btn">Gestão de Estoque</a>
            <a href="enriquecimento" class="btn">Gestão de Enriquecimento</a>
        </div>
        <div class="tarefas">
<table>
    <thead>
        <tr>
            <th>Nome</th>
            <th>Descrição</th>
            <th>Data</th>
            <th>Concluída</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="tarefa" items="${tarefas}">
            <c:if test="${not tarefa.concluida}">
                <tr>
                    <td>${tarefa.nome}</td>
                    <td>${tarefa.texto}</td>
                    <td>${tarefa.dataPConclusao}</td>
                    <td>
                        <form method="post" action="TarefaController">
                            <input type="hidden" name="acao" value="concluida">
                            <input type="hidden" name="id" value="${tarefa.id}">
                            <input type="checkbox"
                                   name="concluida"
                                   onclick="this.form.submit()"
                                   <c:if test="${tarefa.concluida}">checked</c:if>>
                        </form>
                    </td>
                </tr>
            </c:if>
        </c:forEach>
    </tbody>
</table>
</div>

</html>
