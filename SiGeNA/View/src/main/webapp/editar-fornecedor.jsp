<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@taglib uri="jakarta.tags.functions" prefix="fn" %>

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
    <title>SiGeNA - Editar Fornecedor</title>
    <link rel="stylesheet" href="CSS/styleanimais.css">
    <link rel="stylesheet" href="CSS/style.css">
</head>

<body>
<header>
    <div class="titulo">
        <a href="<%= request.getContextPath() + 
            ("GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? 
            "/home.jsp" : "/home.jsp") %>">SiGeNA</a>
    </div>
</header>

<div class="botoes-acoes">
    <a href="FornecedorController?acao=listar" class="btn">Voltar</a>
</div>

<div class="container">
    <h1>Editar Fornecedor</h1>

    <div class="formulario">

        <p><strong>ID:</strong> <c:out value="${fornecedor.id}"/></p>

        <form action="FornecedorController" method="post">

            <label for="nome">Nome do Fornecedor:*</label>
            <input type="text" id="nome" name="nome" class="obrigatorio" value="<c:out value='${fornecedor.nome}'/>" required>

            <label for="telefone">Telefone:</label>
            <input type="text" id="telefone" name="telefone"
                   value="<c:out value='${fornecedor.telefone}'/>">

            <label for="email">Email:</label>
            <input type="email" id="email" name="email"
                   value="<c:out value='${fornecedor.email}'/>">

            <label for="endereco">Endereço:</label>
            <input type="text" id="endereco" name="endereco"
                   value="<c:out value='${fornecedor.endereco}'/>">

            <label for="tipo">Tipo:*</label>
            <select name="tipo" id="tipo" class="obrigatorio">
                <option value="${fornecedor.tipo}">${fornecedor.tipo}</option>

                <c:if test="${fornecedor.tipo != 'ALIMENTO'}">
                    <option value="ALIMENTO">ALIMENTO</option>
                </c:if>

                <c:if test="${fornecedor.tipo != 'MEDICAMENTO'}">
                    <option value="MEDICAMENTO">MEDICAMENTO</option>
                </c:if>

                <c:if test="${fornecedor.tipo != 'EQUIPAMENTO'}">
                    <option value="EQUIPAMENTO">EQUIPAMENTO</option>
                </c:if>

                <c:if test="${fornecedor.tipo != 'HIGIENE E LIMPEZA'}">
                    <option value="HIGIENE E LIMPEZA">HIGIENE E LIMPEZA</option>
                </c:if>

                <c:if test="${fornecedor.tipo != 'ACESSORIOS'}">
                    <option value="ACESSORIOS">ACESSÓRIOS</option>
                </c:if>

                <c:if test="${fornecedor.tipo != 'SERVICOS'}">
                    <option value="SERVICOS">SERVIÇOS</option>
                </c:if>

                <c:if test="${fornecedor.tipo != 'VARIADOS'}">
                    <option value="VARIADOS">VARIADOS</option>
                </c:if>

                <c:if test="${fornecedor.tipo != 'OUTROS'}">
                    <option value="OUTROS">OUTROS</option>
                </c:if>

            </select>

            <label for="descricao">Descrição:</label>
            <textarea id="descricao" name="descricao">
                <c:out value="${fornecedor.descricao}"/>
            </textarea>

            <c:if test="${not empty sessionScope.campoInvalidoErro}">
                <div class="mensagem">
                    <c:out value="${sessionScope.campoInvalidoErro}"/>
                </div>
                <c:remove var="campoInvalidoErro" scope="session"/>
            </c:if>

            <input type="hidden" name="id" value="<c:out value='${fornecedor.id}'/>">
            <input type="hidden" name="acao" value="editar">

            <button type="submit" class="btn-enviar" onclick="return confirm('Salvar alterações? Essas modificações não poderão ser desfeitas.')">Salvar Alterações</button>
        </form>

    </div>
</div>
<script src="JS/verificar-campos.js"></script>
</body>
</html>

