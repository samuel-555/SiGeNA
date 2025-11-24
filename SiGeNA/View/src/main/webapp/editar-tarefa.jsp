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
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SiGeNA - Gestão de Tarefas</title>
        <link rel="stylesheet" href="CSS\style.css">
    </head>
    <body>
        <header>
            <div class="titulo"><a href="<%= request.getContextPath() + ("GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "/home-gerente.jsp" : "/home.jsp") %>">SiGeNA</a></div>
        </header>
        
        <div class="botoes-acoes">
            <a href="TarefaController?acao=listar" class="btn">Voltar</a>
        </div>
        
        <div class="container">
            <h1>Editar Tarefa</h1>
        <div class="formulario">
            <form action="${pageContext.request.contextPath}/TarefaController" method="post">
                <input type="hidden" name="acao" value="editar">
                <input type="hidden" name="id" value="${habitat.id}"/>                

                <label for="nome">Nome da tarefa:</label>
                <input type="text" id="nome" name="nome" placeholder="Ex: Limpar aquário">

                <label for="destinatario">Funcionario encarregado:</label>
                <select name="destinatario" id="destinatario">
                    
                    <c:forEach items="${funcionarios}" var="funcionario">
                        
                        <c:if test="${funcionario.estado == 'ATIVO'}">
                            <option value="${funcionario.id}">${funcionario.nome}:${funcionario.cargo}</option>
                        </c:if>
                    
                    </c:forEach>
                            
                </select>
                
                <label for="data-conclusao">Data para conclusão:</label>
                <input type="date" name="data-conclusao" id="data-conclusao"placeholder="Ex: 10/10/2007">
                

                <button type="submit" class="btn-enviar">Salvar Alterações</button>
            </form>


        </div>
        </div>
    </body>
</html>