
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
            <div class="titulo">SiGeNA</div>
        </header>
        
        <h1>Bem-vindo, <%= sessao.getAttribute("CpfLogado") %>!</h1>
        <c:set var="cargoUsuario" value="${sessionScope.cargoUsuario}" />

        <div class="grid-botoes">
            <a href="AnimalController?acao=listar" class="btn">Gestão de Animais</a>
            <a href="EspeciesController" class="btn">Gestão de Espécies</a>
            <a href="PlanosAlimentaresController" class="btn">Gestão de Planos Alimentares</a>
            <a href="HabitatController?acao=listar" class="btn">Gestão de Habitat</a>
            <a href="tratamentos.jsp" class="btn">Gestão de Tratamentos Medicos</a>
        </div>
        <div class="tarefas">
            
            <%--
                <c:forEach var= "funcionario" items="${funcionarios}">
                    
                <c:set var="tarefas" value="${tarefa.destinatario.cpf == UsuarioLogado.cpf}"/>
                <c:if test="${funcionario.estado == ATIVO}"> <!-- mudar isso aqui só pra cadastro -->
                    
                    <c:if test="${empty tarefas}">
                        <p>Sem tarefas cadastradas para hoje></p>
                    </c:if>
                        
                    <c:forEach var="tarefa" items="${tarefas}">
                        <c:out value="${tarefa}"/>
                    </c:forEach>
                    
                </c:if>
                        
                </c:forEach>
            </c:if>
            <!-- set user.cpf == funcionario.cpf? -->
            
            <c:forEach var= "funcionario" items="${funcionarios}">
                <c:set var="tarefas" value="${tarefa.destinatario.cpf == usuario-cpf}"/>
                
                <c:if test="${empty tarefas}">
                    <p>Sem tarefas cadastradas para hoje></p>
                </c:if>
                  
                <c:if test="${not empty habitats}">--%>
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
        <c:forEach var="tarefa" items="${tarefas}">
            <%--só aparecer as tarefas não concluidas--%>
          <tr>
            <td>${tarefa.nome}</td>
            <td>${tarefa.dataPConclusao}</td>
            <td>
                
                    <c:out value="${tarefa}"/>
                    
                    <label for="manutencao">Precisa de Manutenção</label>
                    <input type="checkbox" id="manutencao" name="manutencao" <c:if test="${habitat.manutencao}">checked</c:if> >
                
                    
                    
                <input type="hidden" name="acao" value="excluir">
                <input type="hidden" name="nome" value="${tarefa.id}">
                <button class="btn-pequeno excluir">Excluir</button>

        </c:forEach>
                    
        </div>
    </body>
</html>
