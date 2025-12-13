<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
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
            <a href="LogoutServlet" class="btn-sair">Sair</a>
        </header>
        <h1>Bem-vindo, <%= sessao.getAttribute("NomeLogado") != null ? sessao.getAttribute("NomeLogado") : sessao.getAttribute("CpfLogado") %>!</h1>
        <div class="grid-botoes">

            
            <a href="AnimalController?acao=listar" class="btn">Gestão de Animais</a>
            <a href="EspeciesController" class="btn">Gestão de Espécies</a>
            <a href="PlanosAlimentaresController" class="btn">Gestão de Planos Alimentares</a>
            <a href="HabitatController?acao=listar" class="btn">Gestão de Habitat</a>
            <a href="tratamentos.jsp" class="btn">Gestão de Tratamentos Medicos</a>
            <a href="RelatorioSaudeController" class="btn">Gestão de Relatórios de Saúde</a>
            <a href="ProdutoController?acao=listar" class="btn">Gestão de Estoque</a>
            <a href="enriquecimento" class="btn">Gestão de Enriquecimento</a>
            <a href="AgendamentoController?acao=listar" class="btn">Gestão de Agendamentos</a>
        </div>
        <div class="tarefas">
            
            <!--  if user = gerente -> cadastrar tarefas -->
            <!-- set user.cpf == funcionario.cpf? -->
            
            <c:forEach var= "funcionario" items="${funcionarios}">
                <c:set var="tarefas" value="${tarefa.destinatario.cpf == usuario.cpf}"/>
                <c:if test="${funcionario.estado == ATIVO}"> <!-- mudar isso aqui só pra cadastro -->
                    
                    <c:if test="${empty tarefas}">
                        <p>Sem tarefas cadastradas para hoje></p>
                    </c:if>
                        
                    <c:forEach var="tarefa" items="${tarefas}">
                        <c:out value="${tarefa}"/>
                    </c:forEach>
                    
                </c:if>
            </c:forEach>
                    
        </div>
    </body>
</html>
