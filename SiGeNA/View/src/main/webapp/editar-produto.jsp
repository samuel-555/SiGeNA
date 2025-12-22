<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="sigena.model.domain.util.TipoProduto"%>
<%@page import="jakarta.servlet.http.HttpSession" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    pageContext.setAttribute("tiposProduto", TipoProduto.values());
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SiGeNA - Editar Produto</title>
        <link rel="stylesheet" href="CSS/styleprodutos.css">
        <link rel="stylesheet" href="CSS/style.css">
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
            <h1>Editar Produto</h1>

            <div class="formulario">
                <h2>Atualizar Informações</h2>

                <form method="POST" action="ProdutoController">

                    <input type="hidden" name="acao" value="salvarEdicao">
                    <input type="hidden" name="id" value="${produto.id}">

                    <label for="nome">Nome do Produto:</label>
                    <input type="text" id="nome" name="nome" value="${produto.nome}" required>

                    <label for="categoria">Categoria:</label>
                    <select id="categoria" name="tipoProduto">
                        <c:forEach var="tipo" items="${tiposProduto}">
                            <option value="${tipo}" <c:if test="${produto.tipo == tipo}">selected</c:if>>
                                ${fn:replace(tipo, "_", " ")}
                            </option>
                        </c:forEach>
                    </select>

                    <label for="quantidade">Quantidade:</label>
                    <input type="number" id="quantidade" name="quantidade" min="1" value="${produto.quantidade}" required>

                    <div id="perecivel" style="display: block;">

                        <label for="lote">Data de Lote:</label>
                        <input type="date" id="lote" name="lote"
                               value="${produto.lote != null ? produto.lote : ''}">

                        <label for="validade">Data de Validade:</label>
                        <input type="date" id="validade" name="validade"
                               value="${produto.validade != null ? produto.validade : ''}">
                    </div>

                    <div class="disponivel-box">
                        <label for="disponivel">Disponível:</label>
                        <input type="checkbox" id="disponivel" name="disponivel" value="true"
                               <c:if test="${produto.disponivel}">checked</c:if>>
                        <span>Produto disponível para listagem</span>
                    </div>

                    <button type="submit" class="btn-enviar">Salvar Alterações</button>
                </form>
            </div>
        </div>

    </body>

    <script>
        const inputLote = document.getElementById("lote");
        const inputValidade = document.getElementById("validade");

        document.getElementById("categoria").addEventListener("change", function () {
            let valor = this.value;
            if (valor === "PERECIVEL") {
                document.getElementById("perecivel").style.display = "block";
            } else {
                document.getElementById("perecivel").style.display = "none";
                inputLote.value = "9999-12-31";
                inputValidade.value = "9999-12-31";
            }
        });
    </script>

</html>
