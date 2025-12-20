<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="sigena.model.domain.Doacao" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    sigena.model.domain.Doacao doacao = (Doacao) request.getAttribute("doacao");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Cadastrar Doação</title>
    <link rel="stylesheet" href="CSS/style.css">
    <link rel="stylesheet" href="CSS/stylefuncionario.css">
</head>
<body>

<header>
    <div class="titulo">
        <a href="<%= request.getContextPath() + "/home.jsp" %>">SiGeNA</a>
    </div>
</header>

<div class="container">
    <h1><%= doacao != null ? "Editar Doação" : "Cadastrar Doação" %></h1>

    <form action="doacoes" method="post">

        <c:choose>
            <c:when test="${doacao != null}">
                <input type="hidden" name="acao" value="atualizar" />
                <input type="hidden" name="id" value="${doacao.id}" />
            </c:when>

            <c:otherwise>
                <input type="hidden" name="acao" value="cadastrar" />
            </c:otherwise>
        </c:choose>

        <label>Nome do Doador:</label>
        <input type="text" name="doador" value="${doacao.nomeDoador}" required />

        <label>Tipo da Doação:</label>
        <select name="tipoDoacao" required>
            <option value="">-- selecione --</option>
            <option value="MONETARIA" ${doacao != null && doacao.tipo == 'MONETARIA' ? 'selected' : ''}>Monetária</option>
            <option value="OUTRO" ${doacao != null && doacao.tipo == 'OUTRO' ? 'selected' : ''}>Outro</option>
        </select>

        <div id="bloco-monetaria" style="${doacao != null && doacao.tipo == 'MONETARIA' ? '' : 'display:none;'}">
            <label>Valor (R$):</label>
            <input type="number" name="valor" value="${doacao.valorMonetario}" min="0" step="0.01"  />
        </div>

        <div id="bloco-outro" style="${doacao == null || doacao.tipo != 'MONETARIA' ? '' : 'display:none;'}">
            <label>Descrição (se outro):</label>
            <input type="text" name="descricaoOutro" value="${doacao.descricaoOutro}" />
        </div>

        <label>Data da Doação:</label>
        <input type="date" name="data" value="${doacao.dataDoacao}" />

        <label>Observações:</label>
        <textarea name="observacoes" rows="3">${doacao.observacoes}</textarea>

        <button type="submit" class="btn">Salvar</button>
        <a href="doacoes" class="btn cancelar">Cancelar</a>
    </form>
</div>

<script>
    const tipoSelect = document.querySelector('select[name="tipoDoacao"]');
    const blocoMon = document.getElementById('bloco-monetaria');
    const blocoOut = document.getElementById('bloco-outro');

    tipoSelect && tipoSelect.addEventListener('change', function() {
        if (this.value === 'MONETARIA') {
            blocoMon.style.display = '';
            blocoOut.style.display = 'none';
        } else {
            blocoMon.style.display = 'none';
            blocoOut.style.display = '';
        }
    });
</script>

</body>
</html>
