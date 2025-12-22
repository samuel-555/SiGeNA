<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%@taglib uri="jakarta.tags.core" prefix="c" %>

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
    <meta charset="UTF-8">
    <title>SiGeNA - Editar Tarefa</title>
    <link rel="stylesheet" href="CSS/style.css">
    <link rel="stylesheet" href="CSS/stylehome.css">
    <link rel="stylesheet" href="CSS/stylefuncionalidades.css">
</head>
<body>

<header class="topbar">
            <a href="HomeController" class="titulo">
                <img src="IMG's/logoSiGeNA-COR2.png" alt="Logo" class="brand-logo">
                <span>SiGeNA</span>
            </a>
            <div class="user-area">
                <a href="LogoutServlet" class="btn-sair">Sair</a>
            </div>
        </header>

<div class="botoes-acoes">
    <a href="HomeController" class="btn">Voltar</a>
</div>

<div class="container">
    <h1>Editar Tarefa</h1>

    <div class="formulario">

        <form action="${pageContext.request.contextPath}/HomeController" method="post">

            <input type="hidden" name="acao" value="editar">
            <input type="hidden" name="id" value="${tarefa.id}">

            <c:if test="${not empty msgErro}">
                <p style="color:red;font-weight:bold">${msgErro}</p>
            </c:if>

            <label for="nome">Nome da tarefa:</label>
            <input type="text" id="nome" name="nome"
                   value="${tarefa.nome}" required>

            <label for="texto">Descrição:</label>
            <textarea id="texto" name="texto" required>${tarefa.texto}</textarea>

            <label for="destinatario">Funcionário encarregado:</label>
            <select name="destinatario" id="destinatario" required>
                <c:forEach items="${funcionarios}" var="funcionario">
                    <c:if test="${funcionario.estado == 'ATIVO'}">
                        <option value="${funcionario.id}"
                            <c:if test="${funcionario.id == tarefa.idDestinatario}">
                                selected
                            </c:if>>
                            ${funcionario.nome} : ${funcionario.cargo.descricao}
                        </option>
                    </c:if>
                </c:forEach>
            </select>

            <label for="data-conclusao">Data para conclusão:</label>
            <input type="datetime-local"
                   id="data-conclusao"
                   name="data-conclusao"
                   value="${tarefa.dataPConclusao.toString().substring(0,16)}"
                   required>

            <button type="submit" class="btn-enviar">
                Salvar Alterações
            </button>

        </form>

    </div>
</div>

</body>
</html>
